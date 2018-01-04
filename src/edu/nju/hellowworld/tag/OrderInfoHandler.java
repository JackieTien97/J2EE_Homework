package edu.nju.hellowworld.tag;

import java.io.IOException;
import java.text.SimpleDateFormat;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import edu.nju.hellowworld.action.business.OrderListBean;

public class OrderInfoHandler extends SimpleTagSupport {
	private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	private SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
	
	public void doTag() throws JspException, IOException {
		System.out.println("orderInfoDoTag");

		try {
			OrderListBean listOrder = (OrderListBean) getJspContext().findAttribute("listOrder");
			JspWriter out = getJspContext().getOut();
			for (int i = 0; i < listOrder.getOrders().size(); i++) {
				if (listOrder.getOrders(i).isStockOut()) {
					out.println("<tr style='background-color:#FF0000'>");
				}
				else {
					out.println("<tr>");
				}
				out.println("<TD align='center'>" + String.format("%0$16s", listOrder.getOrders(i).getOrderId()) + "</TD>");
				out.println("<TD align='center'>" + format.format(listOrder.getOrders(i).getTime()) + "</TD>");
				out.println("<TD align='center'>" + listOrder.getOrders(i).getArticleId() + "</TD>");
				out.println("<TD align='center'>" + listOrder.getOrders(i).getNum() + "</TD>");
				out.println("<TD align='center'>" + listOrder.getOrders(i).getTotalPrice() + "</TD>");
				if (listOrder.getOrders(i).isStockOut()) {
					out.println("<td align='center'>Out of Stock</td></tr>");
				}
				else {
					out.println("<td align='center'>In Stock</td></tr>");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspException(e.getMessage());
		}
	}
}
