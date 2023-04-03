package cn.lingjiatong.re.api.backend.mq;

import lombok.Data;

/**
 * 前端获取系统监控数据消息对象
 *
 * @author Ling, Jiatong
 * Date: 4/3/23 10:55 PM
 */
@Data
public class SystemMonitorMessage {

    /**
     * 用户id
     */
    private String userId;

    /**
     * 获取的消息类型
     *
     * 1 CPU信息
     * 2 内存信息
     * 3 pod信息
     * 4 硬盘信息
     */
    private Integer type;

    /**
     * 主机ip地址（当type=1、2、4时需要传递此参数）
     */
    private String hostIPAddr;

    /**
     * k8s集群的名称空间（当type=3时传递此参数）
     */
    private String namespace;
}
