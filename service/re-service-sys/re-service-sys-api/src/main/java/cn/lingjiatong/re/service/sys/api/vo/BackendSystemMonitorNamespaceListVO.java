package cn.lingjiatong.re.service.sys.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 后台系统监控k8s名称空间列表VO对象
 *
 * @author Ling, Jiatong
 * Date: 4/5/23 3:13 PM
 */
@Data
@Schema(name = "BackendSystemMonitorNamespaceListVO", description = "后台系统监控k8s名称空间列表VO对象")
public class BackendSystemMonitorNamespaceListVO {

    /**
     * 名称
     */
    @Schema(description = "名称")
    private String name;
}
