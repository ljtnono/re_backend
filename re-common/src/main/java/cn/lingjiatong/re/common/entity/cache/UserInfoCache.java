package cn.lingjiatong.re.common.entity.cache;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * 用户信息缓存
 *
 * @author Ling, Jiatong
 * Date: 2022/10/23 00:46
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserInfoCache {

    /**
     * 用户id
     */
    private Long id;

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
    private List<Long> roleIdList;

    /**
     * access_token
     */
    private String accessToken;

    /**
     * token_type
     */
    private String tokenType;

    /**
     * refresh_token
     */
    private String refreshToken;

    /**
     * expires_in
     */
    private Integer expiresIn;

    /**
     * scope
     */
    private Set<String> scope;

    /**
     * jti
     */
    private String jti;

    /**
     * 权限列表
     */
    private List<Long> permissionIdList;

    /**
     * 登陆时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime loginDate;
}
