package com.honeyboard.api.handler;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.honeyboard.api.jwt.model.service.JwtService;
import com.honeyboard.api.user.model.CustomUserDetails;
import com.honeyboard.api.user.model.User;
import com.honeyboard.api.user.model.service.UserService;
import com.honeyboard.api.util.CookieUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	private final JwtService jwtService;
	private final CookieUtil cookieUtil;
	private final UserService userService;
	private final ObjectMapper objectMapper;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		log.debug("OAuth2AuthenticationSuccessHandler/onAuthenticationSuccess");
		CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal(); // 정보 가져오기
		User user = customUserDetails.getUser();

		if (customUserDetails.getAttributes() != null
				&& Boolean.TRUE.equals(customUserDetails.getAttributes().get("isNewUser"))) { // 처음 로그인한 유저면
			String temporaryToken = jwtService.generateTemporaryToken(user.getEmail()); // 임시토큰 발급
			cookieUtil.addCookie(response, "temporary_token", temporaryToken,
					(int) (jwtService.getTemporaryTokenExpire() / 1000)); // httponly cookies로 전달
			response.sendRedirect("http://localhost:5173/oauth/google/additional");
			return;
		} // http://localhost:8080/oauth2/authorization/google

		// 여기까지 오면 기존 유저
		String accessToken = jwtService.generateAccessToken(user); // 토큰 발급
		String refreshToken = jwtService.generateRefreshToken(user);

		cookieUtil.addCookie(response, "access_token", accessToken, (int) (jwtService.getAccessTokenExpire() / 1000)); // httponly
																														// cookies에
																														// 담기
		cookieUtil.addCookie(response, "refresh_token", refreshToken,
				(int) (jwtService.getRefreshTokenExpire() / 1000));

		// 응답 데이터 설정
		User responseUser = new User();
		responseUser.setUserId(user.getUserId());
		responseUser.setName(user.getName());
		responseUser.setGenerationId(user.getGenerationId());

		// JSON 응답 전송
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		objectMapper.writeValue(response.getWriter(), responseUser);

        response.sendRedirect("http://localhost:5173/");
    }

}
