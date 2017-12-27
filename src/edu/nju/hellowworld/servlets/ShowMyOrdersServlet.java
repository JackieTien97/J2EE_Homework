package edu.nju.hellowworld.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import edu.nju.hellowworld.model.Order;


/**
 * Servlet implementation class StockListServlet
 */
@WebServlet("/ShowMyOrders")
public class ShowMyOrdersServlet extends HttpServlet {
	public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	private static final long serialVersionUID = 1L;
	private DataSource datasource = null;
	private SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ShowMyOrdersServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void init() {
		InitialContext jndiContext = null;

		Properties properties = new Properties();
		properties.put(javax.naming.Context.PROVIDER_URL, "jnp:///");
		properties.put(javax.naming.Context.INITIAL_CONTEXT_FACTORY, "org.apache.naming.java.javaURLContextFactory");
		try {
			jndiContext = new InitialContext(properties);
			datasource = (DataSource) jndiContext.lookup("java:comp/env/jdbc/webhomework2");
		} catch (NamingException e) {
			e.printStackTrace();
		}

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		HttpSession session = req.getSession(false);

		if (session == null) {
			resp.sendRedirect(req.getContextPath() + "/Login");
		} else {
			String username = (String) session.getAttribute("login");
			req.setAttribute("login", username);
			getStockList(req, resp);
			displayMyStocklistPage(req, resp);
			displayLogoutPage(req, resp);
		}
	}

	public void getStockList(HttpServletRequest req, HttpServletResponse res) {

		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet result = null;
		List<Order> orders = new ArrayList<>();
		try {
			connection = datasource.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			stmt = connection.prepareStatement("select * from `order` where userid = ? order by time limit ?,10");
			stmt.setString(1, (String) req.getAttribute("login"));
			int page = 0;
			if (req.getQueryString() != null) {
				String[] query = req.getQueryString().split("&");
				page = Integer.valueOf(query[0].split("=")[1]);
			}
			stmt.setInt(2, page * 10);
			result = stmt.executeQuery();
			while (result.next()) {
				Order order = new Order();
				order.setOrderId(result.getInt("orderId"));
				order.setTime(result.getTimestamp("time"));
				order.setArticleId(result.getString("articleId"));
				order.setNum(result.getInt("num"));
				order.setTotalPrice(result.getInt("totalPrice"));
				order.setStockOut(result.getBoolean("isStockOut"));
				orders.add(order);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (result != null) {
				try {
					result.close();
				} catch (Exception ignore) {
					ignore.printStackTrace();
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		req.setAttribute("orders", orders);

	}

	public void displayLogoutPage(HttpServletRequest req, HttpServletResponse res) throws IOException {
		PrintWriter out = res.getWriter();
		ServletContext servletContext = getServletContext();
		out.println("<form method='GET' action='" + res.encodeURL(req.getContextPath() + "/Login") + "'>");
		out.println("</p>");
		out.println("<input type='submit' name='Logout' value='Logout'>");
		out.println("</form>");
		out.println("<p>总人数: " + servletContext.getAttribute("total_people") + "</p>");
		out.println("<p>已登录人数: " + servletContext.getAttribute("login_people") + "</p>");
		out.println("<p>游客人数: " + servletContext.getAttribute("guest") + "</p>");
		out.println("</body></html>");
	}

	public void displayMyStocklistPage(HttpServletRequest req, HttpServletResponse res) throws IOException {
		@SuppressWarnings("unchecked")
		List<Order> orders = (List<Order>) req.getAttribute("orders");
		int page = 0;
		if (req.getQueryString() != null) {
			String[] query = req.getQueryString().split("&");
			page = Integer.valueOf(query[0].split("=")[1]);
		}
		PrintWriter out = res.getWriter();
		out.println("<html><body>");
		out.println("<table width='650' border='0' >");
		out.println("<tr>");
		out.println("<td width='650' height='80' background='" + req.getContextPath() + "/image/top.jpg'>&nbsp;</td>");
		out.println("</tr>");
		out.println("</table>");
		out.println("<p>Welcome " + req.getAttribute("login") + "</p>");

		out.println("<div>");
		if (orders.isEmpty()) {
			// 该用户没有订单存在
			if (page == 0) {
				out.println("<p>Sorry, you have no orders</p>");
				out.println("Click <a href='" + res.encodeURL(req.getRequestURI() + "?page=0") + "'>here</a> to reload this page.<br>");
			}
			// 该用户没有更多的订单
			else {
				out.println("<p>Sorry, you have no more orders</p>");
				String resultURL = req.getRequestURI() + "?page=0";
				out.println("Click <a href='" + res.encodeURL(resultURL) + "'>here</a> to back to first page.<br>");
			}
		}
		else {
			out.println("<table border='1'>");
			out.println("<tr>");
			out.println("<th>OrderId</th>");
			out.println("<th>Time</th>");
			out.println("<th>ArticleId</th>");
			out.println("<th>Number</th>");
			out.println("<th>TotalPrice</th>");
			out.println("<th>State</th>");
			out.println("</tr>");
			for (Order order : orders) {
				if (order.isStockOut()) {
					out.println("<tr style='background-color:#FF0000'>");
				}
				else {
					out.println("<tr>");
				}
				
				out.println("<td>" + String.format("%0$16s", order.getOrderId()) + "</td>");
				out.println("<td>" + format.format(order.getTime()) + "</td>");
				out.println("<td>" + order.getArticleId() + "</td>");
				out.println("<td>" + order.getNum() + "</td>");
				out.println("<td>" + order.getTotalPrice() + "</td>");
				if (order.isStockOut()) {
					out.println("<td>Out of Stock</td>");
				}
				else {
					out.println("<td>In Stock</td>");
				}
				out.println("</tr>");
			}
			
			out.println("</div>");
			out.println("<div>");
			out.println("<ul>");
			if (page > 0) {
				out.println("<li>");
				out.println("<a href='"+ res.encodeURL(req.getContextPath() + "/ShowMyOrders?page=" + (page-1)) +"'>");
				out.println("上一页</a>");
				out.println("</li>");
			}
			out.println("<li>");
			out.println("<a href='"+ res.encodeURL(req.getContextPath() + "/ShowMyOrders?page=" + (page+1)) +"'>");
			out.println("下一页</a>");
			out.println("</li>");
			out.println("</ul>");
			out.println("</div>");
		}
		
	}

}
