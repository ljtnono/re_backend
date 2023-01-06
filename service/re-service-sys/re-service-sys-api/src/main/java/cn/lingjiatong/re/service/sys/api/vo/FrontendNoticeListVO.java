package cn.lingjiatong.re.service.sys.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

/**
 * 前端通知列表VO对象
 *
 * @author Ling, Jiatong
 * Date: 2022/10/15 14:14
 */
@Data
@Schema(name = "FrontendNoticeListVO", description = "前端通知列表VO对象")
public class FrontendNoticeListVO {

    /**
     * 通知标题
     */
    @Schema(description = "通知标题")
    private String title;

    /**
     * 通知链接
     */
    @Schema(description = "通知链接")
    private String link;

    /**
     * 通知类型
     *
     * 1 系统通知（重要） 2 常规通知 3 日常新闻
     */
    @Schema(description = "通知类型 1 系统通知（重要） 2 常规通知 3 日常新闻")
    private Byte type;

    /**
     * 新闻通知类型
     * 0 普通
     * 1 热榜
     * 2 新闻
     */
    @Schema(description = "新闻通知类型 0 普通 1 热榜 2 新闻")
    private Byte newsState;

    /**
     * 新闻通知日期
     */
    @Schema(description = "新闻通知日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate newsDate;
}
