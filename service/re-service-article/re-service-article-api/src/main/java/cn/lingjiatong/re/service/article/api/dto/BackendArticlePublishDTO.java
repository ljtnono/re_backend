package cn.lingjiatong.re.service.article.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 后台文章发布接口DTO对象
 *
 * @author Ling, Jiatong
 * Date: 2022/10/17 22:00
 */
@Data
@Schema(name = "BackendArticlePublishDTO", description = "后台文章发布接口DTO对象")
public class BackendArticlePublishDTO {

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
     * 文章简介
     */
    @Schema(description = "文章简介")
    private String summary;

    /**
     * 文章的markdown格式内容
     */
    @Schema(description = "文章的markdown格式内容")
    private String markdownContent;

    /**
     * 文章的html格式内容
     */
    @Schema(description = "文章的html格式内容")
    private String htmlContent;

    /**
     * 文章所属类型id
     */
    @Schema(description = "文章所属类型id，前端传递字符串类型，后端需要转为Long类型")
    private Long categoryId;

    /**
     * 是否设置为推荐文章
     *
     * 1 是 0 否
     */
    @Schema(description = "是否设置为推荐文章 1 是 0 否")
    private Integer recommend;

    /**
     * 是否设置为置顶
     *
     * 1 是 0 否
     */
    @Schema(description = "是否设置为置顶 1 是 0 否")
    private Integer top;

    /**
     * 创作类型
     *
     * 1 原创 2 转载
     */
    @Schema(description = "创作类型 1 原创 2 转载")
    private Integer creationType;

    /**
     * 转载文章信息
     * 一般是作者信息和原访问地址，markdown形式
     */
    @Schema(description = "转载文章信息")
    private String transportInfo;

    /**
     * 文章引文信息
     */
    @Schema(description = "文章引文信息")
    private String quoteInfo;

    /**
     * 文章封面图片url
     */
    @Schema(description = "文章封面图片url")
    private String coverUrl;

    /**
     * 文章标签列表
     */
    @Schema(description = "文章标签")
    private List<String> tagList;
}
