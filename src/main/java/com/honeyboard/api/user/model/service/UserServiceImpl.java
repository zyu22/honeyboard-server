package com.honeyboard.api.user.model.service;

import org.springframework.stereotype.Service;

import com.honeyboard.api.user.model.User;
import com.honeyboard.api.user.model.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserMapper userMapper;

	@Override
	public Boolean saveUser(User user) {
		return userMapper.insertUser(user) > 0;
	}

	@Override
	public User getUser(int userId) {
		return userMapper.selectUserById(userId);
	}

	@Override
	public User getUserByEmail(String email) {
		return userMapper.selectUserByEmail(email);
	}

	@Override
	public Boolean updatePassword(User user) {
		return userMapper.updatePassword(user) > 0;
	}

	@Override
	public Boolean updateUser(User user) {
		return userMapper.updateUser(user) > 0;
	}

	@Override
	public Boolean updateUserCompletionStatus(int userId) {
		return userMapper.updateUserCompletionStatus(userId) > 0;
	}

	@Override
	public Boolean existsByEmail(String email) {
		return userMapper.selectExistedEmail(email) > 0;
	}

	@Override
	public Integer getActiveGenerationId() {
		return userMapper.selectActiveGenerationId();
	}

}
