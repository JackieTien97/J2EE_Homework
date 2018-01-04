package edu.nju.hellowworld.servlets;

import java.io.IOException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.nju.hellowworld.action.business.OrderListBean;
import edu.nju.hellowworld.factory.ServiceFactory;


/**
 * Servlet implementation class StockListServlet
 */
@WebServlet("/ShowMyOrders")
public class ShowMyOrdersServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ShowMyOrdersServlet() {
		super();
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
			ServletContext context = getServletContext();
			OrderListBean orderListBean = new OrderListBean();
			String username = (String) session.getAttribute("login");
			int page = 0;
			if (req.getQueryString() != null) {
				String[] query = req.getQueryString().split("&");
				page = Integer.valueOf(query[0].split("=")[1]);
			}
			orderListBean.setOrders(ServiceFactory.getOrderManageService().getCustomerOrders(username, page));
			try {
				if (orderListBean.getOrders().size() < 1) {
					if (page == 0) {
						context.getRequestDispatcher("/order/noListOrder.jsp").forward(
								req, resp);
					}
					else {
						context.getRequestDispatcher("/order/noMoreOrder.jsp").forward(
								req, resp);
					}
				} else {
					System.out.println(orderListBean.getOrders().size());
					session.setAttribute("listOrder", orderListBean);
					session.setAttribute("page", page);
					context.getRequestDispatcher("/order/listOrder.jsp").forward(
							req, resp);
				}
			} catch (ServletException e) {
				resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
						"This is a ServletException.");
			}
		}
	}

}
