package cn.lingjiatong.re.service.article.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 前端分页获取文章列表VO对象
 *
 * @author Ling, Jiatong
 * Date: 2023/2/1 14:00
 */
@Data
@Schema(name = "FrontendArticleListVO", description = "前端分页获取文章列表VO对象")
public class FrontendArticleListVO {

    /**
     * id
     */
    @Schema(description = "id")
    private String id;

    /**
     * 作者用户id
     */
    @Schema(description = "作者用户id")
    private Long userId;

    /**
     * 作者
     */
    @Schema(description = "作者")
    private String author;

    /**
     * 文章标题
     */
    @Schema(description = "文章标题")
    private String title;

    /**
     * 文章简介
     */
    @Schema(description = "文章简介")
    private String summary;

    /**
     * 文章分类
     */
    @Schema(description = "文章分类")
    private String category;

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
     * 浏览量
     */
    @Schema(description = "浏览量")
    private Long view;

    /**
     * 最后修改时间
     */
    @Schema(description = "最后修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifyTime;
}
