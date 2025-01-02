package com.honeyboard.api.user.service;

import com.honeyboard.api.user.model.User;

import java.util.List;

public interface UserService {

	Boolean saveUser(User user);

	User getUser(int userId);

	User getUserByEmail(String email);

	Boolean updatePassword(User user);

	Boolean updateUser(User user);

	Boolean updateUserCompletionStatus(int userId);

	Boolean existsByEmail(String email);

	Integer getActiveGenerationId();

	List<User> getAllUsersWithTeamInfo(int generationId);

}
