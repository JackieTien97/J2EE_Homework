package edu.nju.hellowworld.listeners;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Application Lifecycle Listener implementation class CountingListener
 *
 */
@WebListener
public class CountingListener implements ServletContextListener, HttpSessionListener {

	/**
	 * Default constructor.
	 */
	public CountingListener() {
	}

	/**
	 * @see HttpSessionListener#sessionCreated(HttpSessionEvent)
	 */
	public void sessionCreated(HttpSessionEvent se) {
		ServletContext servletContext = se.getSession().getServletContext();
		int total_people = (Integer) servletContext.getAttribute("total_people");
		total_people++;
		int guest = (Integer) servletContext.getAttribute("guest");
		guest++;
		servletContext.setAttribute("total_people", total_people);
		servletContext.setAttribute("guest", guest);
		System.out.println("guest visit");
	}

	/**
	 * @see HttpSessionListener#sessionDestroyed(HttpSessionEvent)
	 */
	public void sessionDestroyed(HttpSessionEvent se) {
		ServletContext servletContext = se.getSession().getServletContext();
		int total_people = (Integer) servletContext.getAttribute("total_people");
		total_people--;
		servletContext.setAttribute("total_people", total_people);
		if ((Boolean) se.getSession().getAttribute("guest")) {
			int guest = (Integer) servletContext.getAttribute("guest");
			guest--;
			servletContext.setAttribute("guest", guest);
			System.out.println("guest logout");
		} else {
			int login_people = (Integer) servletContext.getAttribute("login_people");
			login_people--;
			servletContext.setAttribute("login_people", login_people);
			System.out.println("Someone logout");
		}
		System.out.println("session destroyed");
	}

	/**
	 * @see ServletContextListener#contextDestroyed(ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent sce) {
		System.out.println("Application shut down");
	}

	/**
	 * @see ServletContextListener#contextInitialized(ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext servletContext = sce.getServletContext();
		// 总人数
		servletContext.setAttribute("total_people", 0);
		// 已登录人数
		servletContext.setAttribute("login_people", 0);
		// 游客人数
		servletContext.setAttribute("guest", 0);

		System.out.println("Application initialized");
	}

}
