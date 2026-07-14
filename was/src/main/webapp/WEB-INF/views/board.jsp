<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.cicdlab.app.model.Post" %>
<%@ page import="com.cicdlab.app.model.User" %>
<!DOCTYPE html>
<html lang="ko">
<head><meta charset="UTF-8"><title>게시판</title></head>
<body>
    <h1>📋 게시판 (v7.0 - Full Auto!)</h1>
    <%
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser != null) {
    %>
        <p><b><%= loginUser.getNickname() %></b>님 환영합니다 |
           <a href="<%= request.getContextPath() %>/logout">로그아웃</a></p>
        <form method="post" action="<%= request.getContextPath() %>/board">
            <p><input type="text" name="title" placeholder="제목" size="40"></p>
            <p><textarea name="content" rows="3" cols="42" placeholder="내용"></textarea></p>
            <button type="submit">글 등록</button>
        </form>
    <% } else { %>
        <p><a href="<%= request.getContextPath() %>/login">로그인</a> 후 글을 쓸 수 있습니다. |
           <a href="<%= request.getContextPath() %>/signup">회원가입</a></p>
    <% } %>
    <hr>
    <%
        List<Post> posts = (List<Post>) request.getAttribute("posts");
        if (posts == null || posts.isEmpty()) {
    %>
        <p>아직 게시글이 없습니다.</p>
    <% } else {
           for (Post p : posts) { %>
        <div style="border:1px solid #ccc; margin:8px 0; padding:8px">
            <b>[<%= p.getId() %>] <%= p.getTitle() %></b>
            <small> — <%= p.getWriter() %>, <%= p.getCreatedAt() %></small>
            <p><%= p.getContent() %></p>
        </div>
    <%     }
       } %>
</body>
</html>