package com.honeyboard.api.auth.controller;

import com.honeyboard.api.auth.model.EmailVerification;
import com.honeyboard.api.auth.model.service.EmailService;
import com.honeyboard.api.auth.model.service.VerificationService;
import com.honeyboard.api.jwt.model.service.JwtService;
import com.honeyboard.api.user.model.User;
import com.honeyboard.api.user.model.service.UserService;
import com.honeyboard.api.util.CookieUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final VerificationService verificationService;
    private final CookieUtil cookieUtil;

    @PostMapping("/{domainName}/signup")
    public ResponseEntity<?> completeOAuth2Signup(@PathVariable String domainName, // 구글 네이버 카카오
                                                  @CookieValue("temporary_token") String temporaryToken, // 임시토큰
                                                  @RequestBody User user, HttpServletResponse response) { // 클라이언트에서 받은 유저 이름 담아져있음
        log.debug("AuthController/completeOAuth2Signup");
        String email = jwtService.getEmailFromToken(temporaryToken); // OAuth2로 로그인 인증 되어 임시토큰 가지고 컨트롤러로 도착하면 이메일 추출

        userService.existsByEmail(email);

        // 각종 정보 저장
        user.setEmail(email);
        user.setRole("USER");
        user.setGenerationId(userService.getActiveGenerationId());
        user.setLoginType(domainName.toUpperCase());
        user.setIsSsafy(true);

        userService.saveUser(user);
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        cookieUtil.addCookie(response, "access_token", accessToken,
                (int) (jwtService.getAccessTokenExpire() / 1000));
        cookieUtil.addCookie(response, "refresh_token", refreshToken,
                (int) (jwtService.getRefreshTokenExpire() / 1000));

        User responseUser = new User();
        responseUser.setUserId(user.getUserId());
        responseUser.setName(user.getName());
        responseUser.setGenerationId(user.getGenerationId());

        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody User user, HttpServletResponse response) {
        log.debug("AuthController/signup");

        try {
            if (userService.existsByEmail(user.getEmail())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setGenerationId(userService.getActiveGenerationId());
        user.setLoginType("FORM");

        userService.saveUser(user);

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        cookieUtil.addCookie(response, "access_token", accessToken,
                (int) (jwtService.getAccessTokenExpire() / 1000));
        cookieUtil.addCookie(response, "refresh_token", refreshToken,
                (int) (jwtService.getRefreshTokenExpire() / 1000));

        User responseUser = new User();
        responseUser.setUserId(user.getUserId());
        responseUser.setName(user.getName());
        responseUser.setGenerationId(user.getGenerationId());

        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
    }

    @PostMapping("/email/validation")
    public ResponseEntity<?> validateEmail(@RequestBody EmailVerification req) {
        String email = req.getEmail();
        log.debug("이메일 중복확인 요청 시작 - 이메일: {}", email);
        boolean exists = userService.existsByEmail(req.getEmail());
        return exists ? ResponseEntity.status(HttpStatus.CONFLICT).build()
                : ResponseEntity.ok().build();
    }

    @PostMapping("/email/send")
    public ResponseEntity<?> sendVerificationEmail(@RequestBody EmailVerification req) {
        String email = req.getEmail();
        log.debug("이메일 인증 코드 발송 요청 시작 - 수신자: {}", email);
        String code = verificationService.generateVerificationCode();
        log.debug("인증 코드 생성 완료: {}", code);

        verificationService.saveVerificationCode(email, code);
        log.debug("인증 코드 저장 완료 - 이메일: {}, 코드: {}", email, code);

        emailService.sendVerificationEmail(email, code);
        log.info("이메일 인증 코드 발송 성공 - 수신자: {}", email);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/email/verify")
    public ResponseEntity<?> verifyEmail(@RequestBody EmailVerification emailVerification,
                                         HttpServletResponse response) {
        log.debug("이메일 인증 코드 검증 시작 - 이메일: {}, 코드: {}",
                emailVerification.getEmail(),
                emailVerification.getCode());

        boolean isValid = verificationService.verifyCode(
                emailVerification.getEmail(),
                emailVerification.getCode()
        );

        if (!isValid) {
            log.warn("잘못된 인증 코드 - 이메일: {}, 입력된 코드: {}",
                    emailVerification.getEmail(),
                    emailVerification.getCode());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        log.debug("인증 코드 검증 성공 - 이메일: {}", emailVerification.getEmail());

        String tempToken = jwtService.generateTemporaryToken(emailVerification.getEmail());
        log.debug("임시 토큰 생성 완료 - 이메일: {}", emailVerification.getEmail());

        int expireSeconds = (int) (jwtService.getTemporaryTokenExpire() / 1000);
        cookieUtil.addCookie(response, "temporary_token", tempToken, expireSeconds);
        log.info("임시 토큰 쿠키 설정 완료 - 이메일: {}, 만료시간: {}초",
                emailVerification.getEmail(), expireSeconds);

        return ResponseEntity.ok().build();
    }

}
