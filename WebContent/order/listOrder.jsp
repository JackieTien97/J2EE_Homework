<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="order" uri="/WEB-INF/tlds/orderInfo.tld"%>
<%@ taglib prefix="login" uri="/WEB-INF/tlds/orderInfo.tld"%>

<login:checkLogin />

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Watch List</title>
</head>

<BODY>
	<table width="650" border="0">
		<tr>
			<td width="650" height="80"
				background="<%=request.getContextPath() + "/image/top.jpg"%>">&nbsp;</td>
		</tr>
	</table>
	<H1>Online Stock.</H1>
	<H2>
		<BR>
		<jsp:useBean id="listOrder"
			type="edu.nju.hellowworld.action.business.OrderListBean"
			scope="session"></jsp:useBean>
		<jsp:useBean id="item" class="edu.nju.hellowworld.model.Order"
			scope="page"></jsp:useBean>


		There are all Stocks.
	</H2>
	<H4>
		<TABLE border="1">
			<TBODY>
				<TR>
					<th>OrderId</th>
					<TH>Time</TH>
					<TH>ArticleId</TH>
					<TH>Number</TH>
					<TH>TotalPrice</TH>
					<TH>State</TH>
				</TR>

				<order:orderInfo />
			</TBODY>
		</TABLE>
	</H4>
	
	<div>
		<ul>
			<%if ((Integer)session.getAttribute("page") > 0) {%>
				<li>
					<a href="<%=response.encodeURL(request.getContextPath()) + "/ShowMyOrders?page=" + ((Integer)session.getAttribute("page") - 1)%>">上一页</a>
				</li>
			<%}%>
			<li>
				<a href="<%=response.encodeURL(request.getContextPath()) + "/ShowMyOrders?page=" + ((Integer)session.getAttribute("page") + 1)%>">下一页</a>
			</li>
		</ul>
	</div>
	
	<form method='GET' action='" + res.encodeURL(req.getContextPath() + "/Login") + "'>
		<input type='submit' name='Logout' value='Logout'>
	</form>
	
	<p>总人数: <%=application.getAttribute("total_people")%></p>
	<p>已登录人数: <%=application.getAttribute("login_people")%></p>
	<p>游客人数: <%=application.getAttribute("guest")%></p>"
	
</BODY>
</html>


