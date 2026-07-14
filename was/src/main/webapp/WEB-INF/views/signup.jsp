<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="ko">
<head><meta charset="UTF-8"><title>회원가입</title></head>
<body>
    <h1>📝 회원가입</h1>
    <% String error = (String) request.getAttribute("error");
       if (error != null) { %><p style="color:red"><%= error %></p><% } %>
    <form method="post" action="<%= request.getContextPath() %>/signup">
        <p>아이디: <input type="text" name="username"></p>
        <p>비밀번호: <input type="password" name="password"></p>
        <p>닉네임: <input type="text" name="nickname"></p>
        <button type="submit">가입하기</button>
    </form>
    <p><a href="<%= request.getContextPath() %>/login">로그인으로</a></p>
</body>
</html>