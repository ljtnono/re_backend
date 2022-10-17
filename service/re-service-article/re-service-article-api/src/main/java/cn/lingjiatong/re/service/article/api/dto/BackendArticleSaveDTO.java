package cn.lingjiatong.re.service.article.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 后台保存文章接口DTO对象
 *
 * @author Ling, Jiatong
 * Date: 2022/10/17 22:00
 */
@Data
@ApiModel(description = "后台保存文章接口DTO对象")
public class BackendArticleSaveDTO {

    /**
     * 文章标题
     */
    @ApiModelProperty("文章标题")
    private String title;

    /**
     * 文章简介
     */
    @ApiModelProperty("文章简介")
    private String summary;

    /**
     * 文章的markdown格式内容
     */
    @ApiModelProperty("文章的markdown格式内容")
    private String markdownContent;

    /**
     * 文章的html格式内容
     */
    @ApiModelProperty("文章的html格式内容")
    private String htmlContent;

    /**
     * 文章所属类型id
     */
    @ApiModelProperty("文章所属类型id")
    private Integer categoryId;

    /**
     * 是否设置为推荐文章
     *
     * 1 是 0 否
     */
    @ApiModelProperty("是否设置为推荐文章 1 是 0 否")
    private Integer recommend;

    /**
     * 是否设置为置顶
     *
     * 1 是 0 否
     */
    @ApiModelProperty("是否设置为置顶 1 是 0 否")
    private Integer top;

    /**
     * 创作类型
     *
     * 1 原创 2 转载
     */
    @ApiModelProperty("创作类型 1 原创 2 转载")
    private Integer creationType;

    /**
     * 转载文章信息
     * 一般是作者信息和原访问地址，markdown形式
     */
    @ApiModelProperty("转载文章信息")
    private String transportInfo;

    /**
     * 文章引文信息
     */
    @ApiModelProperty("文章引文信息")
    private String quoteInfo;

    /**
     * 文章封面图片url
     */
    @ApiModelProperty("文章封面图片url")
    private String coverUrl;
}
