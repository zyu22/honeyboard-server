package com.honeyboard.api.auth.model.service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.honeyboard.api.jwt.model.service.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VerificationServiceImpl implements VerificationService {
	
	private final RedisTemplate<String, String> redisTemplate;
	private final JwtService jwtService;

	@Override
	public String generateVerificationCode() {
		return String.format("%06d", new Random().nextInt(999999));
	}

	@Override
	public void saveVerificationCode(String email, String verificationCode) {
		redisTemplate.opsForValue().set(email, verificationCode, 5, TimeUnit.MINUTES);
	}

	@Override
	public boolean verifyCode(String email, String verificationCode) {
		String savedCode = redisTemplate.opsForValue().get(email);
        if (verificationCode.equals(savedCode)) {
            redisTemplate.delete(email);
            return true;
        }
        return false;
	}

}
