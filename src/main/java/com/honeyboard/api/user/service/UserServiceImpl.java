package com.honeyboard.api.user.service;

import com.honeyboard.api.exception.BusinessException;
import com.honeyboard.api.exception.ErrorCode;
import org.springframework.stereotype.Service;

import com.honeyboard.api.user.model.User;
import com.honeyboard.api.user.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserMapper userMapper;

	@Override
	public Boolean saveUser(User user) {
		validateUserInput(user);
		checkDuplicateEmail(user.getEmail());

		userMapper.insertUser(user);
		return true;
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
		if (user == null || user.getPassword() == null || user.getPassword().trim().isEmpty()) {
			throw new IllegalArgumentException("비밀번호를 입력해주세요.");
		}
		return userMapper.updatePassword(user) > 0;
	}

	@Override
	public Boolean updateUser(User user) {
		validateUserInput(user);

		// 이메일이 변경된 경우에만 중복 체크
		User existingUser = getUser(user.getUserId());
		if (!existingUser.getEmail().equals(user.getEmail())) {
			checkDuplicateEmail(user.getEmail());
		}

		userMapper.updateUser(user);
		return true;
	}

	@Override
	public Boolean updateUserCompletionStatus(int userId) {
		if (userMapper.selectUserById(userId) == null) {
			throw new IllegalArgumentException("존재하지 않는 사용자입니다.");
		}
		return userMapper.updateUserCompletionStatus(userId) > 0;
	}

	@Override
	public Boolean existsByEmail(String email) {
		if (email == null || email.trim().isEmpty()) {
			throw new IllegalArgumentException("이메일을 입력해주세요.");
		}
		return userMapper.selectExistedEmail(email) > 0;
	}

	@Override
	public Integer getActiveGenerationId() {
		return userMapper.selectActiveGenerationId();
	}

	@Override
	public
	List<User> getAllUsersWithTeamInfo(int generationId) {
		return userMapper.selectUsersWithTeamInfo(generationId);
	}

	private void validateUserInput(User user) {
		if (user == null) {
			throw new IllegalArgumentException("사용자 정보가 없습니다.");
		}
		if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
			throw new IllegalArgumentException("이메일을 입력해주세요.");
		}
		if (user.getName() == null || user.getName().trim().isEmpty()) {
			throw new IllegalArgumentException("이름을 입력해주세요.");
		}
	}

	private void checkDuplicateEmail(String email) {
		if (userMapper.selectExistedEmail(email) > 0) {
			throw new BusinessException(ErrorCode.DUPLICATE_EMAIL);
		}
	}


}
