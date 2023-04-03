package cn.lingjiatong.re.service.sys.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 后台系统监控内存VO对象
 *
 * @author Ling, Jiatong
 * Date: 4/3/23 9:48 PM
 */
@Data
@Schema(name = "BackendSystemMonitorMemoryVO", description = "后台系统监控内存VO对象")
public class BackendSystemMonitorMemoryVO {

    /**
     * 总内存
     */
    @Schema(description = "总内存")
    private String totalMemory;

    /**
     * 已使用内存
     */
    @Schema(description = "已使用内存")
    private String usedMemory;

    /**
     * 空闲内存
     */
    @Schema(description = "空闲内存")
    private String freeMemory;

    /**
     * 已使用内存百分比
     */
    @Schema(description = "已使用内存百分比")
    private String memoryUsedPercent;

}
