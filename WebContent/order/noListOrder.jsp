<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="login" uri="/WEB-INF/tlds/orderInfo.tld"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<login:checkLogin />

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Watch List</title>
</head>
<body>
	<H1>Online Order.</H1>
	<H2>
		<BR>There are all Orders.
	</H2>
	<H4>
		<BR>You currently do not have any items checked out from the
		order.
	</H4>
	
	<p>总人数: <%=application.getAttribute("total_people")%></p>
	<p>已登录人数: <%=application.getAttribute("login_people")%></p>
	<p>游客人数: <%=application.getAttribute("guest")%></p>

</body>
</html>