package cn.lingjiatong.re.service.sys.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 后台系统监控获取k8s节点列表VO对象
 *
 * @author Ling, Jiatong
 * Date: 4/3/23 8:55 PM
 */
@Data
@Schema(name = "BackendSystemMonitorK8sNodeListVO", description = "后台系统监控获取k8s节点列表VO对象")
public class BackendSystemMonitorK8sNodeListVO {

    /**
     * 节点主机名称
     */
    @Schema(description = "节点主机名称")
    private String nodeHostname;

    /**
     * 节点ip地址
     */
    @Schema(description = "节点ip地址")
    private String nodeIPAddr;
}
