package edu.nju.hellowworld.factory;

import edu.nju.hellowworld.service.OrderManageService;
import edu.nju.hellowworld.service.UserService;
import edu.nju.hellowworld.service.impl.OrderManageServiceImpl;
import edu.nju.hellowworld.service.impl.UserServiceImpl;

public class ServiceFactory {
	
	public static OrderManageService getOrderManageService() {
		return OrderManageServiceImpl.ORDER_MANAGE_SERVICE_IMPL;
	}
	
	public static UserService getUserService() {
		return UserServiceImpl.USER_SERVICE_IMPL;
	}
}
