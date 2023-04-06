package cn.lingjiatong.re.api.backend.websocket.dto;

import lombok.Data;

/**
 * WebSocket消息DTO对象
 *
 * @author Ling, Jiatong
 * Date: 4/6/23 12:24 AM
 */
@Data
public class WebSocketMessageDTO {

    /**
     * 消息类型
     *
     * SYSTEM_MONITOR
     */
    private String type;

    /**
     * 消息体
     */
    private Object body;

}
