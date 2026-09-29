package cn.lingjiatong.re.common.constant;

/**
 * redis缓存健枚举
 *
 * @author Ling, Jiatong
 * Date: 2022/10/17 22:21
 */
public enum RedisCacheKeyEnum {


    // ********************************用户相关********************************

    // 用户信息
    USER_INFO("re:userInfo:"),
    // 验证码key
    LOGIN_VERIFY_CODE("re:verifyCode:"),
    // 绑定邮箱验证码key，拼接用户id
    EMAIL_BIND_CODE("re:email:bindCode:"),
    // 绑定邮箱验证码发送频率限制key，拼接用户id
    EMAIL_BIND_SEND_RATE_LIMIT("re:email:bindRateLimit:"),
    // 修改密码邮箱验证码key，拼接用户id
    EMAIL_UPDATE_PASSWORD_CODE("re:email:passwordCode:"),
    // 修改密码邮箱验证码发送频率限制key，拼接用户id
    EMAIL_UPDATE_PASSWORD_SEND_RATE_LIMIT("re:email:passwordRateLimit:"),

    // ********************************文章相关********************************

    // 文章草稿redis缓存key
    ARTICLE_DRAFT("re:draft:username:draftId"),

    ;
    private final String value;

    public String getValue() {
        return value;
    }

    RedisCacheKeyEnum(String value) {
        this.value = value;
    }
}
