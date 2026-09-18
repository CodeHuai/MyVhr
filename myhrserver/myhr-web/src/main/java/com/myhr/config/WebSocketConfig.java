package com.myhr.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * WebSocket 配置（在线聊天用，对应 vhr 的 WebSocketConfig）
 * TODO 仿写时按教程补充：@ServerEndpoint 注解的 ChatEndpoint（含会话管理、点对点消息）
 */
@Configuration
public class WebSocketConfig {

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
