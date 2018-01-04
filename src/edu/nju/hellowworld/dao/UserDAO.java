package edu.nju.hellowworld.dao;

import edu.nju.hellowworld.model.User;

public interface UserDAO {
	
	User findUserByIdAndPassword(String username, String password);
}
