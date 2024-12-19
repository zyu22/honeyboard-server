package com.honeyboard.api.user.model.service;

import com.honeyboard.api.user.model.User;

public interface UserService {

	Integer saveUser(User user);
	User getUser(long userId);
	User getUserByEmail(String email);
	Integer updatePassword(User user);
	Integer updateUser(User user);
	Integer updateUserCompletionStatus(long userId);
	Integer existsByEmail(String email);
	
}
