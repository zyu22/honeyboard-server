package com.honeyboard.api.auth.service;

public interface EmailService {

	void sendVerificationEmail(String to, String verificationCode);
	
}
