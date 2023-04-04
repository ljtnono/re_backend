package cn.lingjiatong.re.api.backend.websocket;

import cn.lingjiatong.re.api.backend.mq.SystemMonitorMessage;
import cn.lingjiatong.re.common.util.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
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
@ServerEndpoint("/system/monitor")
public class SystemMonitorWebSocketConfig {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    // 用户session map
    public static final ConcurrentHashMap<Long, WebSocketSession> SESSIONS = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(WebSocketSession session) throws Exception {
        String userId = (String) session.getAttributes().get("userId");
        log.info("==========用户：{}，链接websocket服务端", Long.valueOf(userId));
        SESSIONS.put(Long.valueOf(userId), session);
    }

    @OnMessage
    public void onMessage(WebSocketSession session, String textMessage) throws Exception {
        // 处理逻辑  ----   从TextMesasge对象中获取到用户id和想要获取的数据类型等信息，发送给mq
        SystemMonitorMessage systemMonitorMessage = JSONUtil.stringToObject(textMessage, SystemMonitorMessage.class);
        rocketMQTemplate.convertAndSend("", systemMonitorMessage);
    }
}
