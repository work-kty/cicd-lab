<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="ko">
<head><meta charset="UTF-8"><title>로그인</title></head>
<body>
    <h1>🔐 로그인</h1>
    <% if ("1".equals(request.getParameter("joined"))) { %>
        <p style="color:green">가입 완료! 로그인해 주세요.</p>
    <% } %>
    <% String error = (String) request.getAttribute("error");
       if (error != null) { %><p style="color:red"><%= error %></p><% } %>
    <form method="post" action="<%= request.getContextPath() %>/login">
        <p>아이디: <input type="text" name="username"></p>
        <p>비밀번호: <input type="password" name="password"></p>
        <button type="submit">로그인</button>
    </form>
    <p><a href="<%= request.getContextPath() %>/signup">회원가입</a></p>
</body>
</html>