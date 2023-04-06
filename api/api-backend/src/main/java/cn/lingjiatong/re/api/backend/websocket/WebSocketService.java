package cn.lingjiatong.re.api.backend.websocket;

import cn.lingjiatong.re.api.backend.mq.message.SystemMonitorMessage;
import cn.lingjiatong.re.api.backend.security.JwtUtil;
import cn.lingjiatong.re.api.backend.websocket.dto.SystemMonitorMessageDTO;
import cn.lingjiatong.re.api.backend.websocket.dto.WebSocketMessageDTO;
import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.common.entity.User;
import cn.lingjiatong.re.common.exception.ErrorEnum;
import cn.lingjiatong.re.common.util.JSONUtil;
import cn.lingjiatong.re.service.sys.api.client.BackendSystemMonitorFeignClient;
import cn.lingjiatong.re.service.sys.api.vo.BackendSystemMonitorCPUVO;
import cn.lingjiatong.re.service.sys.api.vo.BackendSystemMonitorHardDiskVO;
import cn.lingjiatong.re.service.sys.api.vo.BackendSystemMonitorMemoryVO;
import cn.lingjiatong.re.service.sys.api.vo.BackendSystemMonitorPodListVO;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * websocket处理器
 *
 * @author Ling, Jiatong
 * Date: 4/7/23 2:15 AM
 */
@Slf4j
@Component
public class WebSocketService extends TextWebSocketHandler {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    @Autowired
    private BackendSystemMonitorFeignClient backendSystemMonitorFeignClient;
    @Autowired
    private JwtUtil jwtUtil;
    // 用户session map
    public static final ConcurrentHashMap<String, WebSocketSession> SESSION_MAP = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String username = session.getPrincipal().getName();
        log.info("==========用户：{}，连接websocket服务器", username);
        SESSION_MAP.putIfAbsent(username, session);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage textMessage) throws Exception {
        String message = textMessage.getPayload().toString();
        WebSocketMessageDTO dto = JSONUtil.stringToObject(message, WebSocketMessageDTO.class);
        if (dto == null) {
            ResultVO<?> error = ResultVO.error(ErrorEnum.ILLEGAL_PARAM_ERROR);
            session.sendMessage(new TextMessage(JSONUtil.objectToString(error)));
            return;
        }

        // 通过token，解析出来用户对象
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) ((OAuth2Authentication) session.getPrincipal()).getDetails();
        String token = details.getTokenValue();
        User user = getUserFromToken(token);
        ResultVO<?> resultVO;
        SystemMonitorMessage systemMonitorMessage = new SystemMonitorMessage();
        systemMonitorMessage.setUsername(user.getUsername());
        if ("SYSTEM_MONITOR".equalsIgnoreCase(dto.getType())) {
            // 系统监控
            SystemMonitorMessageDTO systemMonitorMessageDTO = JSONUtil.stringToObject(JSONUtil.objectToString(dto.getBody()), SystemMonitorMessageDTO.class);
            if (systemMonitorMessageDTO == null) {
                ResultVO<?> error = ResultVO.error(ErrorEnum.ILLEGAL_PARAM_ERROR);
                session.sendMessage(new TextMessage(JSONUtil.objectToString(error)));
                return;
            }

            Integer type = systemMonitorMessageDTO.getType();
            if (!Arrays.asList(1, 2, 3, 4).contains(type)) {
                resultVO = ResultVO.error(ErrorEnum.ILLEGAL_PARAM_ERROR);
                session.sendMessage(new TextMessage(JSONUtil.objectToString(resultVO)));
                return;
            }
            if (type.equals(1) || type.equals(2) || type.equals(4)) {
                String hostIPAddr = systemMonitorMessageDTO.getHostIPAddr();
                if (!StringUtils.hasLength(hostIPAddr)) {
                    resultVO = ResultVO.error(ErrorEnum.ILLEGAL_PARAM_ERROR);
                    session.sendMessage(new TextMessage(JSONUtil.objectToString(resultVO)));
                    return;
                }

                try {
                    if (type.equals(1)) {
                        ResultVO<BackendSystemMonitorCPUVO> cpuInfo = backendSystemMonitorFeignClient.findCPUInfo(hostIPAddr, user);
                        systemMonitorMessage.setResultVO(cpuInfo);
                        rocketMQTemplate.convertAndSend("systemMonitor", JSONUtil.objectToString(systemMonitorMessage));
                    } else if (type.equals(2)) {
                        ResultVO<BackendSystemMonitorMemoryVO> memoryInfo = backendSystemMonitorFeignClient.findMemoryInfo(hostIPAddr, user);
                        systemMonitorMessage.setResultVO(memoryInfo);
                        rocketMQTemplate.convertAndSend("systemMonitor", JSONUtil.objectToString(systemMonitorMessage));
                    } else {
                        ResultVO<List<BackendSystemMonitorHardDiskVO>> hardDiskInfo = backendSystemMonitorFeignClient.findHardDiskInfo(hostIPAddr, user);
                        systemMonitorMessage.setResultVO(hardDiskInfo);
                        rocketMQTemplate.convertAndSend("systemMonitor", JSONUtil.objectToString(systemMonitorMessage));
                    }
                } catch (Exception exception) {
                    log.error(exception.toString(), exception);
                    resultVO = ResultVO.error(ErrorEnum.COMMON_SERVER_ERROR);
                    session.sendMessage(new TextMessage(JSONUtil.objectToString(resultVO)));
                }
            } else if (type.equals(3)) {
                String namespace = systemMonitorMessageDTO.getNamespace();
                if (!StringUtils.hasLength(namespace)) {
                    resultVO = ResultVO.error(ErrorEnum.ILLEGAL_PARAM_ERROR);
                    session.sendMessage(new TextMessage(JSONUtil.objectToString(resultVO)));
                    return;
                }

                try {
                    ResultVO<List<BackendSystemMonitorPodListVO>> k8sPodList = backendSystemMonitorFeignClient.findK8sPodList(namespace, user);
                    systemMonitorMessage.setResultVO(k8sPodList);
                    rocketMQTemplate.convertAndSend("systemMonitor", JSONUtil.objectToString(systemMonitorMessage));
                } catch (Exception exception) {
                    log.error(exception.toString(), exception);
                    resultVO = ResultVO.error(ErrorEnum.COMMON_SERVER_ERROR);
                    session.sendMessage(new TextMessage(JSONUtil.objectToString(resultVO)));
                }
            } else {
                // TODO 其他类型报错
                resultVO = ResultVO.error(ErrorEnum.ILLEGAL_PARAM_ERROR);
                session.sendMessage(new TextMessage(JSONUtil.objectToString(resultVO)));
            }
        } else {
            resultVO = ResultVO.error(ErrorEnum.ILLEGAL_PARAM_ERROR);
            session.sendMessage(new TextMessage(JSONUtil.objectToString(resultVO)));
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        String username = session.getPrincipal().getName();
        log.info("==========用户：{}，断开连接", username);
        SESSION_MAP.remove(username);
        session.close();
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }



    /**
     * token
     *
     * @param token token
     * @return 用户对象
     */
    private User getUserFromToken(String token) {
        Claims claims;
        try {
            claims = jwtUtil.getClaimsFromToken(token);
        } catch (Exception e) {
            log.error(e.toString(), e);
            return null;
        }
        User user = new User();
        Long userId = claims.get("userId", Long.class);
        String username = claims.get("username", String.class);
        String email = claims.get("email", String.class);
        String phone = claims.get("phone", String.class);
        user.setId(userId);
        user.setUsername(username);
        user.setEmail(email);
        user.setPhone(phone);
        return user;
    }
}
