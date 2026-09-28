package cn.lingjiatong.re.service.sys.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 后台系统监控磁盘网络IO VO对象
 *
 * @author Ling, Jiatong
 */
@Data
@Schema(name = "BackendSystemMonitorIOVO", description = "后台系统监控磁盘网络IO VO对象")
public class BackendSystemMonitorIOVO {

    /**
     * 磁盘读取速率
     */
    @Schema(description = "磁盘读取速率 KB/s")
    private String diskReadRate;

    /**
     * 磁盘写入速率
     */
    @Schema(description = "磁盘写入速率 KB/s")
    private String diskWriteRate;

    /**
     * 网络接收速率
     */
    @Schema(description = "网络接收速率 KB/s")
    private String networkRecvRate;

    /**
     * 网络发送速率
     */
    @Schema(description = "网络发送速率 KB/s")
    private String networkSendRate;
}
