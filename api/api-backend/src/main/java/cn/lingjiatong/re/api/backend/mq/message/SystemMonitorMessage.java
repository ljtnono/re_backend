package cn.lingjiatong.re.api.backend.mq.message;

import cn.lingjiatong.re.common.ResultVO;
import lombok.Data;

/**
 * 系统监控消息对象
 *
 * @author Ling, Jiatong
 * Date: 2023/4/6 10:15
 */
@Data
public class SystemMonitorMessage {

    /**
     * 用户名称
     */
    private String username;

    /**
     * 消息类型
     */
    private Integer type;

    /**
     * 消息体
     */
    private ResultVO resultVO;
}
