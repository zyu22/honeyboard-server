package com.honeyboard.api.auth.model.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
	
	private final JavaMailSender mailSender;

	@Override
	public void sendVerificationEmail(String to, String verificationCode) {
		SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("이메일 인증");
        message.setText("인증 코드: " + verificationCode);
        mailSender.send(message);
	}

}
