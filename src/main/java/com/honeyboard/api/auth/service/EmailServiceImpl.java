package com.honeyboard.api.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.MimeMessageHelper;


@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    @Value("${logo.image.url}")
    private String logoImageUrl;

    @Override
    public void sendVerificationEmail(String to, String verificationCode) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom("${EMAIL_SENDER}");
            helper.setTo(to);
            helper.setSubject("HoneyBoard 이메일 인증");

            String htmlContent = """
            <div style="max-width: 600px; margin: 0 auto; padding: 20px; font-family: Arial, sans-serif;">
                <div style="text-align: center; margin-bottom: 30px;">
                    <img src="%s" alt="HoneyBoard Logo" style="max-width: 200px;">
                </div>
                <div style="background-color: #f8f9fa; padding: 30px; border-radius: 10px; text-align: center;">
                    <h1 style="color: #ffc107; margin-bottom: 20px;">이메일 인증</h1>
                    <p style="color: #495057; font-size: 16px; margin-bottom: 30px;">
                        HoneyBoard에 오신 것을 환영합니다!<br>
                        아래의 인증 코드를 입력해주세요.
                    </p>
                    <div style="background-color: #fff; padding: 15px; border-radius: 5px; margin-bottom: 30px;">
                        <span style="font-size: 24px; font-weight: bold; color: #212529;">%s</span>
                    </div>
                    <p style="color: #6c757d; font-size: 14px;">
                        본 인증 코드는 5분간 유효합니다.
                    </p>
                </div>
            </div>
        """.formatted(logoImageUrl, verificationCode);

            helper.setText(htmlContent, true);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("이메일 전송 실패", e);
        }
    }

}
