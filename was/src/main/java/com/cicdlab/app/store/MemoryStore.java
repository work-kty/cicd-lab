package com.cicdlab.app.store;

import com.cicdlab.app.model.Post;
import com.cicdlab.app.model.User;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/** DB 대신 쓰는 인메모리 저장소 (싱글톤). Pod 재시작 시 초기화됨 = 의도된 학습 포인트 */
public class MemoryStore {
    private static final MemoryStore INSTANCE = new MemoryStore();
    public static MemoryStore get() { return INSTANCE; }
    private MemoryStore() {}

    private final Map<String, User> users = new ConcurrentHashMap<>();
    private final List<Post> posts = Collections.synchronizedList(new ArrayList<>());
    private final AtomicLong postSeq = new AtomicLong(0);

    public boolean addUser(User u) { return users.putIfAbsent(u.getUsername(), u) == null; }
    public User findUser(String username) { return users.get(username); }

    public void addPost(String title, String content, String writer) {
        posts.add(0, new Post(postSeq.incrementAndGet(), title, content, writer));
    }
    public List<Post> findAllPosts() { return List.copyOf(posts); }
}