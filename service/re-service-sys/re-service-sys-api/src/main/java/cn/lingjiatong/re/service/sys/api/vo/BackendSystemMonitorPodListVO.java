package cn.lingjiatong.re.service.sys.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 后台系统监控pod列表VO对象
 *
 * @author Ling, Jiatong
 * Date: 2023/4/4 09:52
 */
@Data
@Schema(name = "BackendSystemMonitorPodListVO", description = "后台系统监控pod列表VO对象")
public class BackendSystemMonitorPodListVO {

    /**
     * pod名称
     */
    @Schema(description = "pod名称")
    private String name;

    /**
     * ready
     */
    @Schema(description = "ready")
    private String ready;

    /**
     * status
     */
    @Schema(description = "status")
    private String status;

    /**
     * restarts
     */
    @Schema(description = "restarts")
    private String restarts;

    /**
     * age
     */
    @Schema(description = "age")
    private String age;

    /**
     * ip
     */
    @Schema(description = "ip")
    private String ip;

    /**
     * node
     */
    @Schema(description = "node")
    private String node;

    /**
     * nominatedNode
     */
    @Schema(description = "nominatedNode")
    private String nominatedNode;

    /**
     * readinessGates
     */
    @Schema(description = "readinessGates")
    private String readinessGates;
}
