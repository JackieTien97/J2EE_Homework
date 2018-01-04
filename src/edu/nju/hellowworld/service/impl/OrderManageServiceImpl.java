package edu.nju.hellowworld.service.impl;

import java.util.List;

import edu.nju.hellowworld.factory.DAOFactory;
import edu.nju.hellowworld.model.Order;
import edu.nju.hellowworld.service.OrderManageService;

public enum OrderManageServiceImpl implements OrderManageService {
	ORDER_MANAGE_SERVICE_IMPL;

	private OrderManageServiceImpl() {
	}
	
	@Override
	public List<Order> getCustomerOrders(String username, int page) {
		
		return DAOFactory.getOrderDAO().getOrdersById(username, page);
	}

}
