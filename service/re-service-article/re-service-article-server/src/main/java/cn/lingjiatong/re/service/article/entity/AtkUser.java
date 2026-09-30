package cn.lingjiatong.re.service.article.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Artalk评论用户实体
 *
 * @author Ling, Jiatong
 */
@Data
@TableName(value = "users", schema = "artalk")
public class AtkUser {

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
     * 删除时间，软删除标识
     */
    @TableField("deleted_at")
    private LocalDateTime deletedAt;

    /**
     * 用户名
     */
    private String name;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 个人链接
     */
    private String link;

    /**
     * 是否管理员
     */
    @TableField("is_admin")
    private Boolean admin;
}
