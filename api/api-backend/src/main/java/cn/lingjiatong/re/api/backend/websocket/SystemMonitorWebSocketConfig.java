package cn.lingjiatong.re.api.backend.websocket;

import cn.lingjiatong.re.common.util.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 系统监控websocket消息处理
 *
 * @author Ling, Jiatong
 * Date: 4/3/23 10:40 PM
 */
@Slf4j
@Component
@ServerEndpoint("/websocket")
public class SystemMonitorWebSocketConfig {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    // 用户session map
    public static final ConcurrentHashMap<String, Session> SESSION_MAP = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session) {
        String username = session.getUserPrincipal().getName();
        log.info("==========用户：{}，连接websocket服务器", username);
        SESSION_MAP.putIfAbsent(username, session);
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        String username = session.getUserPrincipal().getName();
        try {
            WebSocketMessageDTO dto = JSONUtil.stringToObject(message, WebSocketMessageDTO.class);
        } catch (Exception e) {
            //
        }
    }

    @OnClose
    public void onClose(Session session) {
        String username = session.getUserPrincipal().getName();
        log.info("==========用户：{}，断开连接", username);
        SESSION_MAP.remove(username);
    }

}
