package cn.lingjiatong.re.api.backend.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * 系统监控websocket消息处理
 *
 * @author Ling, Jiatong
 * Date: 4/3/23 10:40 PM
 */
@Slf4j
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler(), "/websocket")
                .setAllowedOrigins("*");
    }

    @Bean
    public WebSocketService webSocketHandler() {
        return new WebSocketService();
    }
}
