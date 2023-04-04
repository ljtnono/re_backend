package cn.lingjiatong.re.api.backend.mq;

import cn.lingjiatong.re.api.backend.websocket.SystemMonitorWebSocketConfig;
import cn.lingjiatong.re.service.sys.api.client.BackendSystemMonitorFeignClient;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 系统监控MQ消息监听器
 *
 * @author Ling, Jiatong
 * Date: 4/3/23 10:31 PM
 */
@Component
@RocketMQMessageListener(topic = "systemMonitor", messageModel = MessageModel.BROADCASTING, consumerGroup = "api-backend")
public class SystemMonitorMessageListener implements RocketMQListener<SystemMonitorMessage> {

    @Autowired
    private BackendSystemMonitorFeignClient backendSystemMonitorFeignClient;

    @Override
    public void onMessage(SystemMonitorMessage message) {
        String userId = message.getUserId();
        ConcurrentHashMap<Long, WebSocketSession> sessions = SystemMonitorWebSocketConfig.SESSIONS;

        // 接受到了rockmq的消息，获取到用户id和接口要获取的数据类型（CPU信息，内存信息，硬盘信息等）
        // 判断sessions中是否存在该用户的id,如果存在，则说明该用户的客户端是的链接是存在本服务中，那么需要调用服务获取到相应的数据并通过SESSIONS中的WebSocketSession对象将数据写回到客户端
        WebSocketSession webSocketSession = sessions.get(Long.valueOf(userId));
        // 构建message对象
        TextMessage textMessage = new TextMessage("");

        try {
            webSocketSession.sendMessage(textMessage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
