package edu.nju.hellowworld.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private DataSource datasource = null;
	
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
	 * @see HttpServlet#HttpServlet()
	 */
	public Login() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String login = "";
		HttpSession session = request.getSession(true);
		session.setMaxInactiveInterval(60);
		if (session.getAttribute("guest") != null) {
			if (!(Boolean)session.getAttribute("guest")) {
				response.sendRedirect(request.getContextPath() + "/ShowMyOrders?page=0");
			}
		}
		else {
			session.setAttribute("guest", true);
		}
		
		Cookie cookie = null;
		Cookie[] cookies = request.getCookies();
		ServletContext servletContext = getServletContext();
		


		if (null != cookies) {
			// Look through all the cookies and see if the
			// cookie with the login info is there.
			for (int i = 0; i < cookies.length; i++) {
				cookie = cookies[i];
				if (cookie.getName().equals("LoginCookie")) {
					login = cookie.getValue();
					break;
				}
			}
		}

		// Logout action removes session, but the cookie remains
		if (null != request.getParameter("Logout")) {
			if (null != session) {
				session.invalidate();
				session = null;
			}
		}

		PrintWriter out = response.getWriter();
		out.println("<html><body>");

		out.println("<form method='POST' action='"
				+ response.encodeURL(request.getContextPath() + "/Login") + "'>");
		out.println("login: <input type='text' name='login' value='" + login + "'>");
		out.println("password: <input type='password' name='password' value=''>");
		out.println("<input type='submit' name='Submit' value='Submit'>");
		out.println("</form>");
		out.println("<p>总人数: " + servletContext.getAttribute("total_people") + "</p>");
		out.println("<p>已登录人数: " + servletContext.getAttribute("login_people") + "</p>");
		out.println("<p>游客人数: " + servletContext.getAttribute("guest") + "</p>");
		out.println("</body></html>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String username = request.getParameter("login");
		String password = request.getParameter("password");
		ServletContext servletContext = getServletContext();
	
		if (username.isEmpty() || password.isEmpty()) { // 用户名或密码为空
			PrintWriter out = response.getWriter();
			out.println("<html><body>");

			out.println("<form method='POST' action='"
					+ response.encodeURL(request.getContextPath() + "/Login") + "'>");
			out.println("login: <input type='text' name='login' value='" + username + "'>");
			out.println("password: <input type='password' name='password' value='" + password + "'>");
			out.println("<input type='submit' name='Submit' value='Submit'></form>");

			out.println("<p>用户名或密码不能为空</p>");

			out.println("<p>总人数: " + servletContext.getAttribute("total_people") + "</p>");
			out.println("<p>已登录人数: " + servletContext.getAttribute("login_people") + "</p>");
			out.println("<p>游客人数: " + servletContext.getAttribute("guest") + "</p>");
			out.println("</body></html>");
		}
		else {
			Connection connection = null;
			PreparedStatement stmt = null;
			ResultSet result = null;
			boolean isValidated = false;
			
			try {
				connection = datasource.getConnection();
				stmt = connection.prepareStatement("select * from user where username=? and password=?");
				stmt.setString(1, username);
				stmt.setString(2, password);
				result = stmt.executeQuery();
				while (result.next()) {
					isValidated = true;
					break;
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
			
			if (isValidated) { // 用户名与密码正确
				HttpSession session = request.getSession(false);
				boolean cookieFound = false;
				Cookie cookie = null;
				Cookie[] cookies = request.getCookies();
				if (null != cookies) {
					// Look through all the cookies and see if the
					// cookie with the login info is there.
					for (int i = 0; i < cookies.length; i++) {
						cookie = cookies[i];
						if (cookie.getName().equals("LoginCookie")) {
							cookieFound = true;
							break;
						}
					}
				}
				if (session == null || !username.equals(session.getAttribute("login"))) {
					if (session.getAttribute("login") == null) {
						session.setAttribute("guest", false);
						int guest = (Integer)servletContext.getAttribute("guest");
						guest--;
						int login_people = (Integer)servletContext.getAttribute("login_people");
						login_people++;
						System.out.println("guest become login");
						servletContext.setAttribute("login_people", login_people);
						servletContext.setAttribute("guest", guest);
					}
					if (cookieFound) { // If the cookie exists update the value only
						// if changed
						if (!username.equals(cookie.getValue())) {
							cookie.setValue(username);
							response.addCookie(cookie);
						}
					} 
					else {
						// If the cookie does not exist, create it and set value
						cookie = new Cookie("LoginCookie", username);
						cookie.setMaxAge(Integer.MAX_VALUE);
						response.addCookie(cookie);
					}
					// create a session to show that we are logged in
					session = request.getSession(true);
					session.setAttribute("login", username);
					
					request.setAttribute("login", username);
					RequestDispatcher requestDispatcher = request.getRequestDispatcher("/ShowMyOrders?page=0");
					requestDispatcher.forward(request, response);
				}
				else {
					session.setAttribute("guest", false);
					int guest = (Integer)servletContext.getAttribute("guest");
					guest--;
					int login_people = (Integer)servletContext.getAttribute("login_people");
					login_people++;
					System.out.println("guest become login");
					servletContext.setAttribute("login_people", login_people);
					servletContext.setAttribute("guest", guest);
					RequestDispatcher requestDispatcher = request.getRequestDispatcher("/ShowMyOrders?page=0");
					requestDispatcher.forward(request, response);
				}
			}
			else { // 用户名或密码错误
				PrintWriter out = response.getWriter();
				out.println("<html><body>");

				out.println("<form method='POST' action='"
						+ response.encodeURL(request.getContextPath() + "/Login") + "'>");
				out.println("login: <input type='text' name='login' value='" + username + "'>");
				out.println("password: <input type='password' name='password' value=''>");
				out.println("<input type='submit' name='Submit' value='Submit'></form>");

				out.println("<p>用户名或密码错误</p>");
				
				out.println("<p>总人数: " + servletContext.getAttribute("total_people") + "</p>");
				out.println("<p>已登录人数: " + servletContext.getAttribute("login_people") + "</p>");
				out.println("<p>游客人数: " + servletContext.getAttribute("guest") + "</p>");

				out.println("</body></html>");
			}
		}
		
	}

}
