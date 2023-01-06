package cn.lingjiatong.re.auth.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * 用户登录VO对象
 *
 * @author Ling, Jiatong
 * Date: 2022/12/29 22:40
 */
@Data
@Schema(name = "UserLoginVO", description = "用户登录VO对象")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserLoginVO {

    /**
     * 用户信息
     */
    @Schema(description = "用户信息")
    private UserInfo userInfo;

    /**
     * 用户拥有的菜单列表
     */
    @Schema(description = "用户菜单列表")
    private List<MenuInfo> menus;

    /**
     * 用户token信息
     */
    @Schema(description = "用户token信息")
    private TokenInfo tokenInfo;


    @Data
    public final static class UserInfo {

        /**
         * 用户id
         */
        @JsonIgnore
        @Schema(hidden = true)
        private Long id;

        /**
         * 用户名
         */
        @Schema(description = "用户名")
        private String username;

        /**
         * 用户邮箱
         */
        @Schema(description = "用户邮箱")
        private String email;

        /**
         * 手机号码
         */
        @Schema(description = "手机号码")
        private String phone;

        /**
         * 用户头像url
         */
        @Schema(description = "用户头像url")
        private String avatarUrl;

        /**
         * 权限id列表
         */
        @Schema(description = "权限id列表")
        private List<Long> permissionIdList;
    }


    @Data
    public final static class MenuInfo {

        /**
         * 菜单id
         */
        @JsonIgnore
        @Schema(hidden = true)
        private Long id;

        /**
         * 父菜单id
         */
        @JsonIgnore
        @Schema(hidden = true)
        private Long parentId;

        /**
         * 菜单标题
         */
        @Schema(description = "菜单标题")
        private String title;

        /**
         * 菜单icon
         */
        @Schema(description = "菜单icon")
        private String icon;

        /**
         * 菜单名称
         */
        @Schema(description = "菜单名称")
        private String name;

        /**
         * 菜单路路径
         */
        @Schema(description = "菜单路径")
        private String path;

        /**
         * 子菜单列表
         */
        @Schema(description = "子菜单列表")
        private List<MenuInfo> children;
    }

    @Data
    public final static class TokenInfo {

        /**
         * access_token
         */
        @JsonProperty("access_token")
        @Schema(description = "access_token")
        private String accessToken;

        /**
         * token_type
         */
        @JsonProperty("token_type")
        @Schema(description = "token_type")
        private String tokenType;

        /**
         * refresh_token
         */
        @JsonProperty("refresh_token")
        @Schema(description = "refresh_token")
        private String refreshToken;

        /**
         * expires_in
         */
        @JsonProperty("expires_in")
        @Schema(description = "expires_in")
        private Integer expiresIn;

        /**
         * scope
         */
        @JsonProperty("scope")
        @Schema(description = "scope")
        private Set<String> scope;

        /**
         * jti
         */
        @JsonProperty("jti")
        @Schema(description = "jti")
        private String jti;

    }

}
