package edu.nju.hellowworld.service;

import java.util.List;

import edu.nju.hellowworld.model.Order;

public interface OrderManageService {
	List<Order> getCustomerOrders(String username, int page);
}
