package cn.lingjiatong.re.service.sys.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 后台系统监控硬盘VO对象
 *
 * @author Ling, Jiatong
 * Date: 2023/4/4 13:53
 */
@Data
@Schema(name = "BackendSystemMonitorHardDiskVO", description = "后台系统监控硬盘VO对象")
public class BackendSystemMonitorHardDiskVO {

    /**
     * 盘符路径
     */
    @Schema(description = "盘符路径")
    private String mountPoint;

    /**
     * 文件系统
     */
    @Schema(description = "文件系统")
    private String fileSystem;

    /**
     * 大小
     */
    @Schema(description = "总大小")
    private String totalSize;

    /**
     * 已用大小
     */
    @Schema(description = "已用大小")
    private String usedSize;

    /**
     * 可用大小
     */
    @Schema(description = "可用大小")
    private String availableSize;

    /**
     * 已用百分比
     */
    @Schema(description = "已用百分比")
    private String usedPercent;

}
