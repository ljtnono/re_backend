package cn.lingjiatong.re.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * 用户实体
 *
 * @author Ling, Jiatong
 * Date: 2022/10/15 22:53
 */
@Data
@TableName(value = "user", schema = "re_sys")
public class User implements UserDetails {

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
    @JsonIgnore
    @TableField(exist = false)
    private Collection<Permission> permissions;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 获取权限列表
        if (CollectionUtils.isEmpty(permissions)) {
            return Lists.newArrayList();
        }
        // 这里的权限在spring security中代表的是角色，spring security会将角色名加上前缀ROLE_
        return permissions.stream().map(permission -> new SimpleGrantedAuthority(permission.getExpression())).collect(Collectors.toList());
    }

    // 用户账户是否没有过期
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 用户凭证是否被锁定
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 用户凭证是否没有过期
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 是否启用用户
    @Override
    public boolean isEnabled() {
        return true;
    }
}
