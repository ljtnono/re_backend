package cn.lingjiatong.re.api.backend.mq;

import cn.lingjiatong.re.api.backend.websocket.SystemMonitorWebSocketConfig;
import cn.lingjiatong.re.service.sys.api.client.BackendSystemMonitorFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
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
@Slf4j
@Component
@RocketMQMessageListener(
        topic = "systemMonitor",
        consumerGroup = "api-backend"
)
public class SystemMonitorMessageListener implements RocketMQListener<String> {

    @Autowired
    private BackendSystemMonitorFeignClient backendSystemMonitorFeignClient;

    @Override
    public void onMessage(String message) {
        log.info("receive message: " + message);
    }

}
