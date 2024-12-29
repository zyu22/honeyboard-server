package com.honeyboard.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Value("${frontend.url}")
    private String frontendUrl;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue"); // 구독 경로
        registry.setApplicationDestinationPrefixes("/app"); // 메세지 발신 경로
        //- 기수별 채팅방: /topic/generation/{generationId}
        //클라이언트 -> 서버 메시지 전송시 사용
        // 예: /app/chat, /app/message 등
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").setAllowedOrigins(frontendUrl)
                .withSockJS()
                .setStreamBytesLimit(512 * 1024)  // 512KB
                .setHttpMessageCacheSize(1000)
                .setDisconnectDelay(30 * 1000);   // 30초
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
        registry
                .setMessageSizeLimit(128 * 1024)      // 메시지 크기 제한 128KB
                .setSendBufferSizeLimit(512 * 1024)   // 버퍼 크기 512KB
                .setSendTimeLimit(20 * 1000);         // 전송 시간 제한 20초
    }
}
