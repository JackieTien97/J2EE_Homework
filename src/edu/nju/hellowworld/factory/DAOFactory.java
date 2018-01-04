package edu.nju.hellowworld.factory;

import edu.nju.hellowworld.dao.OrderDAO;
import edu.nju.hellowworld.dao.UserDAO;
import edu.nju.hellowworld.dao.impl.OrderDAOImpl;
import edu.nju.hellowworld.dao.impl.UserDAOImpl;

public class DAOFactory {
	
	public static OrderDAO getOrderDAO() {
		return OrderDAOImpl.ORDER_DAO_IMPL;
	}
	
	public static UserDAO getUserDAO() {
		return UserDAOImpl.USER_DAO_IMPL;
	}
}
