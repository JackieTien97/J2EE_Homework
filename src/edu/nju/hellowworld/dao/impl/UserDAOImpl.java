package edu.nju.hellowworld.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import edu.nju.hellowworld.dao.DAOHelper;
import edu.nju.hellowworld.dao.UserDAO;
import edu.nju.hellowworld.model.User;

public enum UserDAOImpl implements UserDAO {
	USER_DAO_IMPL;

	private static DAOHelper daoHelper = DAOHelperImpl.DAO_HELPER_IMPL;
	
	private UserDAOImpl() {
		
	}
	
	@Override
	public User findUserByIdAndPassword(String username, String password) {
		Connection connection = daoHelper.getConnection();
		PreparedStatement stmt = null;
		ResultSet result = null;
		User user = null;
		
		try {
			stmt = connection.prepareStatement("select * from user where username=? and password=?");
			stmt.setString(1, username);
			stmt.setString(2, password);
			result = stmt.executeQuery();
			while (result.next()) {
				user = new User();
				break;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			daoHelper.closeConnection(connection);
			daoHelper.closePreparedStatement(stmt);
			daoHelper.closeResult(result);
		}
		
		return user;
	}

}
