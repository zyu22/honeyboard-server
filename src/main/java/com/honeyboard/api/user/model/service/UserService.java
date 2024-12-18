package com.honeyboard.api.user.model.service;

import com.honeyboard.api.user.model.User;

public interface UserService {

	Integer addUser(User user);
	User getUserById(long userId);
	User getUserByEmail(String email);
	Integer changePassword(User user);
	Integer updateUserInfo(User user);
	Integer updateUserCompletionStatus(long userId);
	Integer checkEmail(String email);
	
}
