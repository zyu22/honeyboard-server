package com.honeyboard.api.user.model.mapper;

import com.honeyboard.api.user.model.User;

import java.util.List;

public interface UserMapper {

	Integer insertUser(User user);

	User selectUserById(int userId);

	User selectUserByEmail(String email);

	Integer updatePassword(User user);

	Integer updateUser(User user);

	Integer updateUserCompletionStatus(int userId);

	Integer selectExistedEmail(String email);

	Integer selectActiveGenerationId();

	List<User> selectUsersWithTeamInfo(int generationId);

}
