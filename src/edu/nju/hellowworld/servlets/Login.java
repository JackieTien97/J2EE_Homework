package edu.nju.hellowworld.servlets;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.nju.hellowworld.factory.ServiceFactory;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;

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
			if (!(Boolean) session.getAttribute("guest")) {
				response.sendRedirect(request.getContextPath() + "/ShowMyOrders?page=0");
				return;
			}
		} else {
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

		
		request.setAttribute("login", login);
		request.setAttribute("password", "");
		request.setAttribute("message", "");
		servletContext.getRequestDispatcher("/user/login.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String username = request.getParameter("login");
		String password = request.getParameter("password");
		request.setAttribute("login", username);
		request.setAttribute("password", password);
		ServletContext servletContext = getServletContext();

		if (username.isEmpty() || password.isEmpty()) { // 用户名或密码为空
			request.setAttribute("message", "用户名或密码不能为空");
			servletContext.getRequestDispatcher("/user/login.jsp").forward(request, response);
		} else {
			boolean isValidated = ServiceFactory.getUserService().isValidate(username, password);
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
						int guest = (Integer) servletContext.getAttribute("guest");
						guest--;
						int login_people = (Integer) servletContext.getAttribute("login_people");
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
					} else {
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
				} else {
					session.setAttribute("guest", false);
					int guest = (Integer) servletContext.getAttribute("guest");
					guest--;
					int login_people = (Integer) servletContext.getAttribute("login_people");
					login_people++;
					System.out.println("guest become login");
					servletContext.setAttribute("login_people", login_people);
					servletContext.setAttribute("guest", guest);
					RequestDispatcher requestDispatcher = request.getRequestDispatcher("/ShowMyOrders?page=0");
					requestDispatcher.forward(request, response);
				}
			} else { // 用户名或密码错误
				request.setAttribute("message", "用户名或密码错误");
				servletContext.getRequestDispatcher("/user/login.jsp").forward(request, response);
			}
		}

	}

}
