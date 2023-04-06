package cn.lingjiatong.re.api.backend.websocket;

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

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
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

    private static RocketMQTemplate rocketMQTemplate;
    private static BackendSystemMonitorFeignClient backendSystemMonitorFeignClient;
    private static JwtUtil jwtUtil;

    @Autowired
    public void setRocketMQTemplate(RocketMQTemplate rocketMQTemplate) {
        SystemMonitorWebSocketConfig.rocketMQTemplate = rocketMQTemplate;
    }
    @Autowired
    public void setBackendSystemMonitorFeignClient(BackendSystemMonitorFeignClient backendSystemMonitorFeignClient) {
        SystemMonitorWebSocketConfig.backendSystemMonitorFeignClient = backendSystemMonitorFeignClient;
    }
    @Autowired
    public void setJwtUtil(JwtUtil jwtUtil) {
        SystemMonitorWebSocketConfig.jwtUtil = jwtUtil;
    }

    // 用户session map
    public static final ConcurrentHashMap<String, Session> SESSION_MAP = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session) {
        String username = session.getUserPrincipal().getName();
        log.info("==========用户：{}，连接websocket服务器", username);
        SESSION_MAP.putIfAbsent(username, session);
    }

    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        WebSocketMessageDTO dto = JSONUtil.stringToObject(message, WebSocketMessageDTO.class);
        if (dto == null) {
            ResultVO<?> error = ResultVO.error(ErrorEnum.ILLEGAL_PARAM_ERROR);
            session.getBasicRemote().sendText(JSONUtil.objectToString(error));
            return;
        }

        // 通过token，解析出来用户对象
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) ((OAuth2Authentication) session.getUserPrincipal()).getDetails();
        String token = details.getTokenValue();
        User user = getUserFromToken(token);
        ResultVO<?> resultVO;
        if ("SYSTEM_MONITOR".equalsIgnoreCase(dto.getType())) {
            // 系统监控
            SystemMonitorMessageDTO systemMonitorMessageDTO = JSONUtil.stringToObject(JSONUtil.objectToString(dto.getBody()), SystemMonitorMessageDTO.class);
            if (systemMonitorMessageDTO == null) {
                ResultVO<?> error = ResultVO.error(ErrorEnum.ILLEGAL_PARAM_ERROR);
                session.getBasicRemote().sendText(JSONUtil.objectToString(error));
                return;
            }

            Integer type = systemMonitorMessageDTO.getType();
            if (!Arrays.asList(1, 2, 3, 4).contains(type)) {
                resultVO = ResultVO.error(ErrorEnum.ILLEGAL_PARAM_ERROR);
                session.getBasicRemote().sendText(JSONUtil.objectToString(resultVO));
                return;
            }
            if (type.equals(1) || type.equals(2) || type.equals(4)) {
                String hostIPAddr = systemMonitorMessageDTO.getHostIPAddr();
                if (!StringUtils.hasLength(hostIPAddr)) {
                    resultVO = ResultVO.error(ErrorEnum.ILLEGAL_PARAM_ERROR);
                    session.getBasicRemote().sendText(JSONUtil.objectToString(resultVO));
                    return;
                }

                try {
                    if (type.equals(1)) {
                        ResultVO<BackendSystemMonitorCPUVO> cpuInfo = backendSystemMonitorFeignClient.findCPUInfo(hostIPAddr, user);
                        rocketMQTemplate.convertAndSend("systemMonitor", JSONUtil.objectToString(cpuInfo));
                    } else if (type.equals(2)) {
                        ResultVO<BackendSystemMonitorMemoryVO> memoryInfo = backendSystemMonitorFeignClient.findMemoryInfo(hostIPAddr, user);
                        rocketMQTemplate.convertAndSend("systemMonitor", JSONUtil.objectToString(memoryInfo));
                    } else {
                        ResultVO<List<BackendSystemMonitorHardDiskVO>> hardDiskInfo = backendSystemMonitorFeignClient.findHardDiskInfo(hostIPAddr, user);
                        rocketMQTemplate.convertAndSend("systemMonitor", JSONUtil.objectToString(hardDiskInfo));
                    }
                } catch (Exception exception) {
                    log.error(exception.toString(), exception);
                    resultVO = ResultVO.error(ErrorEnum.COMMON_SERVER_ERROR);
                    session.getBasicRemote().sendText(JSONUtil.objectToString(resultVO));
                }
            } else if (type.equals(3)) {
                String namespace = systemMonitorMessageDTO.getNamespace();
                if (!StringUtils.hasLength(namespace)) {
                    resultVO = ResultVO.error(ErrorEnum.ILLEGAL_PARAM_ERROR);
                    session.getBasicRemote().sendText(JSONUtil.objectToString(resultVO));
                    return;
                }

                try {
                    ResultVO<List<BackendSystemMonitorPodListVO>> k8sPodList = backendSystemMonitorFeignClient.findK8sPodList(namespace, user);
                    rocketMQTemplate.convertAndSend("systemMonitor", JSONUtil.objectToString(k8sPodList));
                } catch (Exception exception) {
                    log.error(exception.toString(), exception);
                    resultVO = ResultVO.error(ErrorEnum.COMMON_SERVER_ERROR);
                    session.getBasicRemote().sendText(JSONUtil.objectToString(resultVO));
                }
            } else {
                // TODO 其他类型报错
                resultVO = ResultVO.error(ErrorEnum.ILLEGAL_PARAM_ERROR);
                session.getBasicRemote().sendText(JSONUtil.objectToString(resultVO));
            }
        } else {
            resultVO = ResultVO.error(ErrorEnum.ILLEGAL_PARAM_ERROR);
            session.getBasicRemote().sendText(JSONUtil.objectToString(resultVO));
        }
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        String username = session.getUserPrincipal().getName();
        log.info("==========用户：{}，断开连接", username);
        SESSION_MAP.remove(username);
        session.close();
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
