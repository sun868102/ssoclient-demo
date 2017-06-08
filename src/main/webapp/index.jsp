<%@page import="net.bingosoft.demo.Utils"%>
<%@ page import="net.bingosoft.oss.ssoclient.model.Authentication" %>
<%@ page import="net.bingosoft.oss.ssoclient.SSOUtils" %>
<%@ page import="net.bingosoft.demo.LoginServlet" %>
<%@ page import="net.bingosoft.oss.ssoclient.internal.Urls" %><%--
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
    <%
    	String logoutUrl=Urls.getServerContextUrl(request)+"/logout";
    %>
    <!--
    省公安厅开发测试环境sso:http://114.67.33.50:7077/ssov3
    本地开发测试sso:http://localhost:8089/ssov3
    -->
    恭喜！<%= authentication.getUsername() %> 已经登录！<a href="<%=logoutUrl%>">注销</a>
</body>
</html>
