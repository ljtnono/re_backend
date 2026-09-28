package cn.lingjiatong.re.service.sys.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 后台系统监控CPUVO对象
 *
 * @author Ling, Jiatong
 * Date: 4/3/23 9:26 PM
 */
@Data
@Schema(name = "BackendSystemMonitorCPUVO", description = "后台系统监控CPUVO对象")
public class BackendSystemMonitorCPUVO {

    /**
     * cpu核心数
     */
    @Schema(description = "cpu核心数")
    private Integer cpuCoreNum;

    /**
     * cpu用户使用率
     */
    @Schema(description = "用户使用率")
    private String userUsedPercent;


    /**
     * cpu系统使用率
     */
    @Schema(description = "系统使用率")
    private String systemUsedPercent;

    /**
     * cpu当前空闲率
     */
    @Schema(description = "当前空闲率")
    private String freePercent;

    /**
     * 1分钟负载
     */
    @Schema(description = "1分钟负载")
    private String loadAverage1;

    /**
     * 5分钟负载
     */
    @Schema(description = "5分钟负载")
    private String loadAverage5;

    /**
     * 15分钟负载
     */
    @Schema(description = "15分钟负载")
    private String loadAverage15;
}
