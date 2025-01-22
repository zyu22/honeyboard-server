package com.honeyboard.api.auth.service;

public interface VerificationService {

	String generateVerificationCode();
	void saveVerificationCode(String email, String verificationCode);
	boolean verifyCode(String email, String verificationCode);
	
}
