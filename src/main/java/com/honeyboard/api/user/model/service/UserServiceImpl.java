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
	public Integer saveUser(User user) {
		return userMapper.insertUser(user);
	}

	@Override
	public User getUser(long userId) {
		return userMapper.selectUserById(userId);
	}

	@Override
	public User getUserByEmail(String email) {
		return userMapper.selectUserByEmail(email);
	}

	@Override
	public Integer updatePassword(User user) {
		return userMapper.updatePassword(user);
	}

	@Override
	public Integer updateUser(User user) {
		return userMapper.updateUser(user);
	}

	@Override
	public Integer updateUserCompletionStatus(long userId) {
		return userMapper.updateUserCompletionStatus(userId);
	}

	@Override
	public Integer existsByEmail(String email) {
		return userMapper.selectExistedEmail(email);
	}

}
