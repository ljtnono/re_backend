package cn.lingjiatong.re.common.entity.cache;

import cn.lingjiatong.re.common.entity.Permission;
import cn.lingjiatong.re.common.entity.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;

/**
 * 用户信息缓存
 *
 * @author Ling, Jiatong
 * Date: 2022/10/23 00:46
 */
@Data
public class UserInfoCache {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 角色列表
     */
    private Collection<Role> roles;

    /**
     * token
     */
    private String token;

    /**
     * 权限列表
     */
    private Collection<Permission> permissions;

    /**
     * 登陆时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime loginDate;
}
