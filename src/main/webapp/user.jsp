<%@page import="java.util.Map"%>
<%@ page import="net.bingosoft.oss.ssoclient.model.Authentication" %><%--
  Created by IntelliJ IDEA.
  User: KAEL
  Date: 2017/5/16
  Time: 20:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>用户信息</title>
</head>
<body>
    当前用户名：<%= ((Authentication)request.getSession().getAttribute("loginUser")).getUsername() %>
    <br/>
    用户详细属性：<br/><br/>
    <%
	Map<String,Object> attrs = ((Authentication)request.getSession().getAttribute("loginUser")).getAttributes();
	for (Map.Entry<String, Object> entry :attrs.entrySet()) {
		out.println(entry.getKey() + ":" + entry.getValue()+ "<BR><BR>");
	}
	%>

    <a href="/demo/index.jsp">回到首页</a>
</body>
</html>
