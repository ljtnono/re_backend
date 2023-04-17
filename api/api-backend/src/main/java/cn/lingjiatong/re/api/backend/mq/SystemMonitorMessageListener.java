package cn.lingjiatong.re.api.backend.mq;

import cn.lingjiatong.re.api.backend.mq.message.SystemMonitorMessage;
import cn.lingjiatong.re.api.backend.websocket.WebSocketConfig;
import cn.lingjiatong.re.api.backend.websocket.WebSocketService;
import cn.lingjiatong.re.common.util.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.websocket.Session;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 系统监控MQ消息监听器
 *
 * @author Ling, Jiatong
 * Date: 4/3/23 10:31 PM
 */
@Slf4j
@Component
@RocketMQMessageListener(
        topic = "systemMonitor",
        consumerGroup = "api-backend",
        consumeMode = ConsumeMode.CONCURRENTLY,
        messageModel = MessageModel.BROADCASTING
)
public class SystemMonitorMessageListener implements RocketMQListener<String> {

    @Override
    public void onMessage(String message) {
        SystemMonitorMessage systemMonitorMessage = JSONUtil.stringToObject(message, SystemMonitorMessage.class);
        String username = systemMonitorMessage.getUsername();
        ConcurrentHashMap<String, WebSocketSession> sessions = WebSocketService.SESSION_MAP;
        // 接受到了rockmq的消息，获取到用户id和接口要获取的数据类型（CPU信息，内存信息，硬盘信息等）
        // 判断sessions中是否存在该用户的id,如果存在，则说明该用户的客户端是的链接是存在本服务中，那么需要调用服务获取到相应的数据并通过SESSIONS中的WebSocketSession对象将数据写回到客户端
        WebSocketSession session = sessions.get(username);
        if (session != null) {
            try {
                if (session.isOpen()) {
                    session.sendMessage(new TextMessage(JSONUtil.objectToString(systemMonitorMessage)));
                }
            } catch (IOException e) {
                log.error(e.toString(), e);
            }
        }
        // 如果该用户不在当前节点中，那么不做任何处理
    }

}
