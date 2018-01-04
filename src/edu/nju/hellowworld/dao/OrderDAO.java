package edu.nju.hellowworld.dao;

import java.util.List;

import edu.nju.hellowworld.model.Order;

public interface OrderDAO {
	List<Order> getOrdersById(String username, int page);
}
