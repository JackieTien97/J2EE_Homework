package edu.nju.hellowworld.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.nju.hellowworld.dao.DAOHelper;
import edu.nju.hellowworld.dao.OrderDAO;
import edu.nju.hellowworld.model.Order;

public enum OrderDAOImpl implements OrderDAO {
	ORDER_DAO_IMPL;
	
	private static DAOHelper daoHelper = DAOHelperImpl.DAO_HELPER_IMPL;
	
	private OrderDAOImpl() {
		
	}
	
	@Override
	public List<Order> getOrdersById(String username, int page) {
		Connection connection = daoHelper.getConnection();
		PreparedStatement stmt = null;
		ResultSet result = null;
		List<Order> orders = new ArrayList<>();
		
		try {
			stmt = connection.prepareStatement("select * from `order` where userid = ? order by time limit ?,10");
			stmt.setString(1, username);
			stmt.setInt(2, page * 10);
			result = stmt.executeQuery();
			while (result.next()) {
				Order order = new Order();
				order.setOrderId(result.getInt("orderId"));
				order.setTime(result.getTimestamp("time"));
				order.setArticleId(result.getString("articleId"));
				order.setNum(result.getInt("num"));
				order.setTotalPrice(result.getInt("totalPrice"));
				order.setStockOut(result.getBoolean("isStockOut"));
				orders.add(order);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			daoHelper.closeConnection(connection);
			daoHelper.closePreparedStatement(stmt);
			daoHelper.closeResult(result);
		}
		
		return orders;
	}

}
