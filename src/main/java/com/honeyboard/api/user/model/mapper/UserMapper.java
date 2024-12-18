package com.honeyboard.api.user.model.mapper;

import com.honeyboard.api.user.model.User;

public interface UserMapper {
	
	Integer insertUser(User user);
	User selectUserById(long userId);
	User selectUserByEmail(String email);
	Integer updatePassword(User user);
	Integer updateUser(User user);
	Integer updateUserCompletionStatus(long userId);
	Integer selectExistedEmail(String email);

}
