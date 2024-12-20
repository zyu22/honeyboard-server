package com.honeyboard.api.auth.model.service;

public interface EmailService {

	void sendVerificationEmail(String to, String verificationCode);
	
}
