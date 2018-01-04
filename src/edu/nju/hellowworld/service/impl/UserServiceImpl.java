package edu.nju.hellowworld.service.impl;

import edu.nju.hellowworld.factory.DAOFactory;
import edu.nju.hellowworld.service.UserService;

public enum UserServiceImpl implements UserService {
	USER_SERVICE_IMPL;

	private UserServiceImpl() {

	}
	@Override
	public boolean isValidate(String username, String password) {
		boolean isValidate = false;
		if (DAOFactory.getUserDAO().findUserByIdAndPassword(username, password) != null) {
			isValidate = true;
		}
		return isValidate;
	}

}
