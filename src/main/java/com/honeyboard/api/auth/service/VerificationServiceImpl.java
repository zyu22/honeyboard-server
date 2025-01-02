package com.honeyboard.api.auth.service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.honeyboard.api.exception.VerificationCodeException;
import com.honeyboard.api.exception.VerificationCodeExpiredException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.honeyboard.api.jwt.model.service.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Slf4j
public class VerificationServiceImpl implements VerificationService {

	private final RedisTemplate<String, String> redisTemplate;
	private final JwtService jwtService;

	@Override
	public String generateVerificationCode() {
		try {
			String code = String.format("%04d", new Random().nextInt(10000));
			log.debug("인증 코드 생성 완료: {}", code);
			return code;
		} catch (Exception e) {
			log.error("인증 코드 생성 실패: {}", e.getMessage());
			throw new VerificationCodeException("인증 코드 생성에 실패했습니다.");
		}
	}

	@Override
	public void saveVerificationCode(String email, String verificationCode) {
		if (email == null || email.trim().isEmpty()) {
			throw new IllegalArgumentException("이메일 주소가 필요합니다.");
		}
		if (verificationCode == null || verificationCode.trim().isEmpty()) {
			throw new IllegalArgumentException("인증 코드가 필요합니다.");
		}

		try {
			redisTemplate.opsForValue().set(email, verificationCode, 5, TimeUnit.MINUTES);
			log.debug("인증 코드 저장 완료 - 이메일: {}", email);
		} catch (Exception e) {
			log.error("인증 코드 저장 실패 - 이메일: {}, 오류: {}", email, e.getMessage());
			throw new VerificationCodeException("인증 코드 저장에 실패했습니다.");
		}
	}

	@Override
	public boolean verifyCode(String email, String verificationCode) {
		if (email == null || email.trim().isEmpty()) {
			throw new IllegalArgumentException("이메일 주소가 필요합니다.");
		}
		if (verificationCode == null || verificationCode.trim().isEmpty()) {
			throw new IllegalArgumentException("인증 코드가 필요합니다.");
		}

		try {
			String savedCode = redisTemplate.opsForValue().get(email);
			if (savedCode == null) {
				throw new VerificationCodeExpiredException("인증 코드가 만료되었습니다.");
			}

			if (!verificationCode.equals(savedCode)) {
				throw new VerificationCodeException("잘못된 인증 코드입니다.");
			}

			redisTemplate.delete(email);
			log.debug("인증 코드 검증 성공 - 이메일: {}", email);
			return true;

		} catch (VerificationCodeExpiredException | VerificationCodeException e) {
			throw e;
		} catch (Exception e) {
			log.error("인증 코드 검증 실패 - 이메일: {}, 오류: {}", email, e.getMessage());
			throw new VerificationCodeException("인증 코드 검증 중 오류가 발생했습니다.");
		}
	}

}
