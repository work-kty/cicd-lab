package com.cicdlab.app.store;

import com.cicdlab.app.model.Post;
import com.cicdlab.app.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** MariaDB 기반 저장소. 접속 정보는 전부 환경변수(ConfigMap/Secret)에서 주입받는다 */
public class DbStore {
    private static final DbStore INSTANCE = new DbStore();
    public static DbStore get() { return INSTANCE; }

    private final String url;
    private final String user;
    private final String password;
    private volatile boolean initialized = false;

    static {
        try {
            Class.forName("org.mariadb.jdbc.Driver");   // 드라이버 클래스를 강제 로드 → 자기 등록
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MariaDB 드라이버 로드 실패 - WAR에 jar가 포함됐는지 확인", e);
        }
    }

    private DbStore() {
        String host = env("DB_HOST", "localhost");
        String port = env("DB_PORT", "3306");
        String db   = env("DB_NAME", "boarddb");
        this.url = "jdbc:mariadb://" + host + ":" + port + "/" + db;
        this.user = env("DB_USER", "boarduser");
        this.password = env("DB_PASSWORD", "");
    }

    private static String env(String key, String def) {
        String v = System.getenv(key);
        return (v == null || v.isBlank()) ? def : v;
    }

    private Connection conn() throws SQLException {
        Connection c = DriverManager.getConnection(url, user, password);
        ensureInit(c);
        return c;
    }

    /** 최초 1회 테이블 생성 (DB가 늦게 뜰 수 있으니 lazy 초기화) */
    private synchronized void ensureInit(Connection c) throws SQLException {
        if (initialized) return;
        try (Statement st = c.createStatement()) {
            st.execute("""
                CREATE TABLE IF NOT EXISTS users (
                  username VARCHAR(50) PRIMARY KEY,
                  password VARCHAR(100) NOT NULL,
                  nickname VARCHAR(50) NOT NULL
                )""");
            st.execute("""
                CREATE TABLE IF NOT EXISTS posts (
                  id BIGINT AUTO_INCREMENT PRIMARY KEY,
                  title VARCHAR(200) NOT NULL,
                  content TEXT,
                  writer VARCHAR(50) NOT NULL,
                  created_at DATETIME DEFAULT CURRENT_TIMESTAMP
                )""");
        }
        initialized = true;
    }

    // ── 회원 ──
    public boolean addUser(User u) {
        String sql = "INSERT INTO users (username, password, nickname) VALUES (?, ?, ?)";
        try (Connection c = conn(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, u.getUsername());
            ps.setString(2, u.getPassword());
            ps.setString(3, u.getNickname());
            ps.executeUpdate();
            return true;
        } catch (SQLIntegrityConstraintViolationException dup) {
            return false;                       // PK 중복 = 이미 존재하는 아이디
        } catch (SQLException e) {
            throw new RuntimeException("DB 오류(addUser)", e);
        }
    }

    public User findUser(String username) {
        String sql = "SELECT username, password, nickname FROM users WHERE username = ?";
        try (Connection c = conn(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return new User(rs.getString(1), rs.getString(2), rs.getString(3));
            }
        } catch (SQLException e) {
            throw new RuntimeException("DB 오류(findUser)", e);
        }
    }

    // ── 게시글 ──
    public void addPost(String title, String content, String writer) {
        String sql = "INSERT INTO posts (title, content, writer) VALUES (?, ?, ?)";
        try (Connection c = conn(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, title);
            ps.setString(2, content);
            ps.setString(3, writer);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("DB 오류(addPost)", e);
        }
    }

    public List<Post> findAllPosts() {
        String sql = "SELECT id, title, content, writer, DATE_FORMAT(created_at, '%Y-%m-%d %H:%i') "
                   + "FROM posts ORDER BY id DESC";
        List<Post> result = new ArrayList<>();
        try (Connection c = conn(); Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                result.add(new Post(rs.getLong(1), rs.getString(2),
                        rs.getString(3), rs.getString(4), rs.getString(5)));
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException("DB 오류(findAllPosts)", e);
        }
    }
}