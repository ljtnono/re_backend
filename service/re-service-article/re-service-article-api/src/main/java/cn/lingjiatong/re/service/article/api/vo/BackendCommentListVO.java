package cn.lingjiatong.re.service.article.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 后台获取评论列表VO对象
 *
 * @author Ling, Jiatong
 */
@Data
@Schema(name = "BackendCommentListVO", description = "后台获取评论列表VO对象")
public class BackendCommentListVO {

    /**
     * 评论id
     */
    @Schema(description = "评论id")
    private Long id;

    /**
     * 评论内容
     */
    @Schema(description = "评论内容")
    private String content;

    /**
     * 评论人昵称
     */
    @Schema(description = "评论人昵称")
    private String nickname;

    /**
     * 所属文章标题
     */
    @Schema(description = "所属文章标题")
    private String articleTitle;

    /**
     * 评论所属页面标识（文章id）
     */
    @Schema(description = "评论所属页面标识（文章id）")
    private String pageKey;

    /**
     * 评论ip
     */
    @Schema(description = "评论ip")
    private String ip;

    /**
     * 回复的评论id，0表示不是回复
     */
    @Schema(description = "回复的评论id，0表示不是回复")
    private Long rid;

    /**
     * 是否待审核
     */
    @Schema(description = "是否待审核")
    private Boolean pending;

    /**
     * 是否置顶
     */
    @Schema(description = "是否置顶")
    private Boolean pinned;

    /**
     * 是否折叠
     */
    @Schema(description = "是否折叠")
    private Boolean collapsed;

    /**
     * 点赞数
     */
    @Schema(description = "点赞数")
    private Long voteUp;

    /**
     * 点踩数
     */
    @Schema(description = "点踩数")
    private Long voteDown;

    /**
     * 评论时间
     */
    @Schema(description = "评论时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
}
