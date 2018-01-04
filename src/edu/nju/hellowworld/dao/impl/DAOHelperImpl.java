package edu.nju.hellowworld.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import edu.nju.hellowworld.dao.DAOHelper;

public enum DAOHelperImpl implements DAOHelper {
	DAO_HELPER_IMPL;
	
	private InitialContext jndiContext = null;
	private Connection connection = null;
	private DataSource datasource = null;

	private DAOHelperImpl() {
		Properties properties = new Properties();
		properties.put(javax.naming.Context.PROVIDER_URL, "jnp:///");
		properties.put(javax.naming.Context.INITIAL_CONTEXT_FACTORY,
		"org.apache.naming.java.javaURLContextFactory");

		try {
			jndiContext = new InitialContext(properties);
			datasource = (DataSource) jndiContext.lookup("java:comp/env/jdbc/webhomework2");
		} catch (NamingException e) {
			e.printStackTrace();
		}	
		System.out.println("got context");
		System.out.println("About to get ds---DaoHelper");
	}
	
	@Override
	public Connection getConnection() {
		try {
			connection = datasource.getConnection();
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}

	@Override
	public void closeConnection(Connection con) {
		if(con!=null) {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void closePreparedStatement(PreparedStatement stmt) {
		if(stmt!=null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void closeResult(ResultSet result) {
		if(result!=null) {
			try {
				result.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
