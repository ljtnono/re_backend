package cn.lingjiatong.re.service.article.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 单个文章信息VO对象
 *
 * @author Ling, Jiatong
 * Date: 2022/10/20 21:40
 */
@Data
@ApiModel(description = "单个文章信息VO对象")
public class FrontendArticleVO {

    /**
     * 主键id
     */
    @ApiModelProperty("id")
    private String id;

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
     * 文章markdown格式内容
     */
    @ApiModelProperty("文章markdown格式内容")
    private String markdownContent;

    /**
     * 文章html格式内容
     */
    @ApiModelProperty("文章html格式内容")
    private String htmlContent;

    /**
     * 文章类型
     */
    @ApiModelProperty("文章分类")
    private String category;

    /**
     * 文章作者id
     */
    @ApiModelProperty("文章作者")
    private String author;

    /**
     * 文章封面图链接地址
     */
    @ApiModelProperty("封面")
    private String coverUrl;

    /**
     * 文章浏览量
     */
    @ApiModelProperty("文章浏览量")
    private Long view;

    /**
     * 文章喜欢数
     */
    @ApiModelProperty("文章喜欢数")
    private Long favorite;

    /**
     * 是否推荐
     * 0 不是 1 是
     */
    @ApiModelProperty("是否推荐 0 不是 1 是")
    private Byte recommend;

    /**
     * 是否置顶
     * 0 不是 1 是
     */
    @ApiModelProperty("是否置顶 0 不是 1 是")
    private Byte top;

    /**
     * 创作类型
     * 1 原创 2 转载
     */
    @ApiModelProperty("创作类型 0 不是 1 是")
    private Byte creationType;

    /**
     * 转载信息
     */
    @ApiModelProperty("转载信息")
    private String transportInfo;

    /**
     * 引用信息
     */
    @ApiModelProperty("引用信息")
    private String quoteInfo;

    /**
     * 最后修改时间
     */
    @ApiModelProperty("最后修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime finalUpdateTime;

    /**
     * 文章标签列表
     */
    @ApiModelProperty("文章标签列表")
    private List<String> tagList;

}
