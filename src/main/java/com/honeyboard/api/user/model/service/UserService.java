package com.honeyboard.api.user.model.service;

import com.honeyboard.api.user.model.User;

public interface UserService {

	User addUser(User user);
	User getUserById(int userId);
	User getUserByEmail(String email);
	Integer changePassword(User user);
	User updateUserInfo(User user);
	Integer changeCompletion(int userId);
	Integer checkEmail(String email);
	
}
