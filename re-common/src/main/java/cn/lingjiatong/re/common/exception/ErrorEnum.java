package cn.lingjiatong.re.common.exception;

/**
 * 错误消息枚举类
 *
 * @author Ling, Jiatong
 * Date: 2021/9/5 1:07 上午
 */
public enum ErrorEnum {


    /** 400 Bad Request */
    // 请求参数为空 ｜ 请求参数格式有误
    REQUEST_PARAM_ERROR(400001, "请求参数有误"),
    // 参数值不在合法的范围内
    ILLEGAL_PARAM_ERROR(400002, "异常的参数值"),
    // 请求对象不存在
    RESOURCE_NOT_EXIST_ERROR(400003, "请求资源不存在"),



    /** 权限相关异常 **/
    // 未认证的用户
    USER_NOT_AUTHTICATE_ERROR(401, "未认证过的用户"),
    // 没有访问权限
    PERMISSION_DENIED_ERROR(403, "禁止访问"),
    // token格式异常
    TOKEN_ILLEGAL_FORMAT_ERROR(401401, "不支持的token格式"),
    // token已过期
    TOKEN_EXPIRED_ERROR(403403, "token已过期，请重新登录"),


    /** 500 Bad Request */
    // MINIO服务器出现异常
    MINIO_SERVER_ERROR(500001, "图片服务器异常，上传失败"),



    /** 600 业务类异常 */
    SAVE_ARTICLE_ERROR(600001, "保存文章失败"),
    LOGIN_VERIFY_CODE_EXPIRED_ERROR(600002, "验证码已过期，请刷新验证码后再试"),

    ;

    /**
     * 错误码
     */
    private final Integer code;

    /**
     * 错误信息
     */
    private final String message;

    ErrorEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
