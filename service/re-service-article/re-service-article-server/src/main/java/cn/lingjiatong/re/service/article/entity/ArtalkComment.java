package cn.lingjiatong.re.service.article.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Artalk评论实体
 *
 * @author Ling, Jiatong
 */
@Data
@TableName(value = "comments", schema = "artalk")
public class ArtalkComment {

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 创建时间
     */
    @TableField("created_at")
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @TableField("updated_at")
    private LocalDateTime updatedAt;

    /**
     * 删除时间，软删除标识
     */
    @TableField("deleted_at")
    private LocalDateTime deletedAt;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 评论所属页面标识（文章id）
     */
    @TableField("page_key")
    private String pageKey;

    /**
     * 站点名称
     */
    @TableField("site_name")
    private String siteName;

    /**
     * 评论用户id
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 回复的评论id，0表示不是回复
     */
    private Long rid;

    /**
     * 是否折叠
     */
    @TableField("is_collapsed")
    private Boolean collapsed;

    /**
     * 是否待审核
     */
    @TableField("is_pending")
    private Boolean pending;

    /**
     * 是否置顶
     */
    @TableField("is_pinned")
    private Boolean pinned;

    /**
     * 点赞数
     */
    @TableField("vote_up")
    private Long voteUp;

    /**
     * 点踩数
     */
    @TableField("vote_down")
    private Long voteDown;

    /**
     * 根评论id，用于嵌套回复树
     */
    @TableField("root_id")
    private Long rootId;

    /**
     * 用户邮箱是否已验证
     */
    @TableField("is_verified")
    private Boolean verified;
}
