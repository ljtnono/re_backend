package cn.lingjiatong.re.auth.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

/**
 * 用户登录VO对象
 *
 * @author Ling, Jiatong
 * Date: 2022/12/29 22:40
 */
@Data
@ApiModel(description = "用户登录VO对象")
public class UserLoginVO {

    /**
     * 用户信息
     */
    @ApiModelProperty("用户信息")
    private UserInfo userInfo;

    /**
     * 用户拥有的菜单列表
     */
    @ApiModelProperty("用户菜单列表")
    private List<MenuInfo> menus;

    /**
     * 用户token信息
     */
    @ApiModelProperty("用户token信息")
    private TokenInfo tokenInfo;


    @Data
    public final static class UserInfo {

        /**
         * 用户名
         */
        @ApiModelProperty("用户名")
        private String username;

        /**
         * 用户邮箱
         */
        @ApiModelProperty("用户邮箱")
        private String email;

        /**
         * 手机号码
         */
        @ApiModelProperty("手机号码")
        private String phone;

        /**
         * 用户头像url
         */
        @ApiModelProperty("用户头像url")
        private String avatarUrl;
    }


    @Data
    public final static class MenuInfo {

        /**
         * 菜单标题
         */
        @ApiModelProperty("菜单标题")
        private String title;

        /**
         * 菜单icon
         */
        @ApiModelProperty("菜单icon")
        private String icon;

        /**
         * 菜单名称
         */
        @ApiModelProperty("菜单名称")
        private String name;

        /**
         * 子菜单列表
         */
        @ApiModelProperty("子菜单列表")
        private List<MenuInfo> children;
    }

    @Data
    public final static class TokenInfo {

        /**
         * access_token
         */
        @JsonProperty("access_token")
        @ApiModelProperty("access_token")
        private String accessToken;

        /**
         * token_type
         */
        @JsonProperty("token_type")
        @ApiModelProperty("token_type")
        private String tokenType;

        /**
         * refresh_token
         */
        @JsonProperty("refresh_token")
        @ApiModelProperty("refresh_token")
        private String refreshToken;

        /**
         * expires_in
         */
        @JsonProperty("expires_in")
        @ApiModelProperty("expires_in")
        private Long expiresIn;

        /**
         * scope
         */
        @JsonProperty("scope")
        @ApiModelProperty("scope")
        private String scope;

        /**
         * jti
         */
        @JsonProperty("jti")
        @ApiModelProperty("jti")
        private String jti;

    }

}
