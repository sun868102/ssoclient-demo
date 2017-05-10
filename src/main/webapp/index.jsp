<%@ page import="net.bingosoft.oss.ssoclient.model.Authentication" %><%--
  Created by IntelliJ IDEA.
  User: KAEL
  Date: 2017/5/10
  Time: 23:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>登录完成</title>
</head>
<body>
    <% Authentication authentication = (Authentication)request.getSession().getAttribute("loginUser"); %>
    恭喜！<%= authentication.getUsername() %> 已经登录！<a href="http://sso.example.com/oauth2/logout">注销</a>
</body>
</html>
