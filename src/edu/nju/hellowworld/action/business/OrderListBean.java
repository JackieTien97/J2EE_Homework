package edu.nju.hellowworld.action.business;

import java.io.Serializable;
import java.util.List;

import edu.nju.hellowworld.model.Order;

public class OrderListBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2817976135182006533L;

	private List<Order> orders;

	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}
	
	public void setOrders(Order order, int index) {
		orders.set(index, order);
	}
	
	public Order getOrders(int index) {
		return orders.get(index);
	}
}
