package cn.lingjiatong.re.service.article.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 文章草稿列表VO对象
 *
 * @author Ling, Jiatong
 * Date: 2022/12/27 09:34
 */
@Data
@Schema(name = "BackendDraftListVO", description = "文章草稿列表VO对象")
public class BackendDraftListVO {

    /**
     * 草稿id
     */
    @Schema(description = "草稿id")
    private String draftId;

    /**
     * 文章标题
     */
    @Schema(description = "文章标题")
    private String title;

    /**
     * 草稿保存时间
     */
    @Schema(description = "草稿保存时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime saveTime;

}
