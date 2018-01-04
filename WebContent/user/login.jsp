<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Login</title>
</head>
<body>
	<form method='POST' action="<%=response.encodeURL(request.getContextPath() + "/Login")%>">
		login: <input type='text' name='login' value="<%=request.getAttribute("login")%>">
		password: <input type='password' name='password' value="<%=request.getAttribute("password")%>">
		<input type='submit' name='Submit' value='Submit'>
	</form>

	<p><%=request.getAttribute("message")%></p>

	<p>总人数: <%=application.getAttribute("total_people")%></p>
	<p>已登录人数: <%=application.getAttribute("login_people")%></p>
	<p>游客人数: <%=application.getAttribute("guest")%></p>
</body>
</html>