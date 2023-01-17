package cn.lingjiatong.re.service.article.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 前端推荐文章列表VO对象
 *
 * @author Ling, Jiatong
 * Date: 2023/1/17 11:11
 */
@Data
@Schema(name = "FrontendArticleRecommendListVO", description = "前端推荐文章列表VO对象")
public class FrontendArticleRecommendListVO {

    /**
     * id
     */
    @Schema(description = "id")
    private String id;

    /**
     * 标题
     */
    @Schema(description = "标题")
    private String title;

    /**
     * 文章封面
     */
    @Schema(description = "文章封面")
    private String coverUrl;

    /**
     * 喜欢数
     */
    @Schema(description = "喜欢数")
    private Long favorite;

    /**
     * 最后修改时间
     */
    @Schema(description = "最后修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifyTime;
}
