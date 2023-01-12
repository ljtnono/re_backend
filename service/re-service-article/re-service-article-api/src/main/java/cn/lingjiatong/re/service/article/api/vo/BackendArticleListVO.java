package cn.lingjiatong.re.service.article.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 后端获取文章列表VO对象
 *
 * @author Ling, Jiatong
 * Date: 2023/1/9 17:00
 */
@Data
@Schema(name = "BackendArticleListVO", description = "后端获取文章列表VO对象")
public class BackendArticleListVO {

    /**
     * 文章id
     */
    @Schema(description = "文章id")
    private String id;

    /**
     * 用户id
     */
    @Schema(description = "用户id", hidden = true)
    private Long userId;

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
     * 文章作者
     */
    @Schema(description = "文章作者")
    private String author;

    /**
     * 浏览量
     */
    @Schema(description = "浏览量")
    private Long view;

    /**
     * 喜欢数
     */
    @Schema(description = "喜欢数")
    private Long favorite;

    /**
     * 是否推荐 0 不推荐 1 推荐
     */
    @Schema(description = "是否推荐 0 不推荐 1 推荐")
    private Byte recommend;

    /**
     * 是否置顶 0 不置顶 1 置顶
     */
    @Schema(description = "是否置顶 0 不置顶 1 置顶")
    private Byte top;

    /**
     * 标签列表
     */
    @Schema(description = "标签列表")
    private List<String> tagList;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 最后修改时间
     */
    @Schema(description = "最后修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime modifyTime;

    /**
     * 是否删除 0 正常 1 已删除
     */
    @Schema(description = "是否删除 0 正常 1 已删除")
    private Byte deleted;
}
