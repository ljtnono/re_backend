package cn.lingjiatong.re.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Collection;

/**
 * 用户实体
 *
 * @author Ling, Jiatong
 * Date: 2022/10/15 22:53
 */
@Data
@TableName(value = "user", schema = "re_sys")
public class User {

    /**
     * 主键id
     */
    @TableId
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 用户头像
     */
    private String avatarUrl;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 最后修改时间
     */
    private LocalDateTime modifyTime;

    /**
     * 是否删除
     *
     * 1 删除
     * 0 正常
     */
    @TableField("is_deleted")
    private Byte deleted;

    /**
     * 角色列表
     */
    @TableField(exist = false)
    private Collection<Role> roles;

    /**
     * 权限列表
     */
    @TableField(exist = false)
    private Collection<Permission> permissions;
}
