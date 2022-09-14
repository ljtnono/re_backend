package cn.ljtnono.re.entity.dto.blog.article;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 发布博客文章DTO对象
 *
 * @author Ling, Jiatong
 * Date: 2021/9/6 12:02 上午
 */
@Data
@ApiModel(description = "发布博客文章DTO对象")
public class PublishArticleDTO {

    /**
     * 文章标题
     */
    @ApiModelProperty(value = "文章标题", required = true)
    private String title;

    /**
     * 文章markdown内容
     */
    @ApiModelProperty(value = "文章的markdown内容", required = true)
    private String markdownContent;

    /**
     * 文章分类id
     */
    @ApiModelProperty(value = "文章分类id", required = true)
    private Integer typeId;

    /**
     * 作者id
     */
    @ApiModelProperty(value = "作者id", required = true)
    private Integer userId;

    /**
     * 博客文章封面图片url
     * 如不传使用默认封面
     */
    @ApiModelProperty(value = "博客文章封面图片url")
    private String coverUrl;

    /**
     * 文章标签列表
     */
    @ApiModelProperty("文章标签列表")
    private List<String> tagList;

    /**
     * 是否是草稿
     *
     * 0 不是
     * 1 是
     */
    @ApiModelProperty(value = "是否是草稿", required = true, notes = "是否是草稿 0 不是 1 是")
    private Integer draft;

    /**
     * 是否推荐到首页
     *
     * 0 不是
     * 1 是
     */
    @ApiModelProperty(value = "是否推荐首页", required = true, notes = "是否推荐首页 0 不是 1 是")
    private Integer recommend;

}
