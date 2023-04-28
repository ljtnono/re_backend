package cn.lingjiatong.re.common.exception;

/**
 * 错误消息枚举类
 *
 * @author Ling, Jiatong
 * Date: 2021/9/5 1:07 上午
 */
public enum ErrorEnum {


    //#################### 400异常 ####################//
    // 请求参数为空 ｜ 请求参数格式有误
    REQUEST_PARAM_ERROR(400001, "请求参数有误"),
    // 参数值不在合法的范围内
    ILLEGAL_PARAM_ERROR(400002, "异常的参数值"),
    // 请求对象不存在
    RESOURCE_NOT_EXIST_ERROR(400003, "请求资源不存在"),



    //#################### 权限相关异常 ####################//
    //#################### 没有携带token或者token异常 ####################//
    // 未认证过的用户
    USER_NOT_AUTHENTICATE_ERROR(401, "未认证过的用户"),
    // token格式异常
    TOKEN_ILLEGAL_FORMAT_ERROR(401401, "不支持的token格式"),
    // token已过期
    TOKEN_EXPIRED_ERROR(401402, "token已过期，请重新登录"),
    // 用户已下线，请重新登录
    USER_ALREADY_LOGOUT_ERROR(401403, "用户已注销，请重新登录"),
    // 用户名或密码错误
    USERNAME_OR_PASSWORD_ERROR(401404, "用户名或密码错误"),
    // 错误的scope值
    INVALID_SCOPE_ERROR(401405, "错误的scope值"),
    // 请输入grant_type的值
    MISSING_GRANT_TYPE_ERROR(400406, "请输入grant_type的值"),
    // 不支持的grant_type
    UNSUPPORTED_GRANT_TYPE_ERROR(401407, "不支持的grant_type"),
    // client_id或client_secret错误
    CLIENT_ID_OR_CLIENT_SECRET_ERROR(401408, "client_id或client_secret错误"),
    // 缺少token参数或token格式异常
    INVALID_TOKEN_ERROR(401409, "缺少token参数或token格式异常"),

    //#################### 携带了token但是没有访问权限异常 ####################//
    // 禁止访问
    PERMISSION_DENIED_ERROR(403, "禁止访问"),
    // 无法修改其他用户信息
    CAN_NOT_UPDATE_OTHER_USER_ERROR(403401, "无法修改其他用户信息"),
    // 没有权限
    NO_PERMISSION_ERROR(403402, "没有操作权限"),

    //#################### 500服务器异常 ####################//
    // 通用系统异常消息
    COMMON_SERVER_ERROR(500000, "系统异常，操作失败"),
    // MINIO服务器出现异常
    MINIO_SERVER_ERROR(500001, "图片服务器异常，上传失败"),
    // 数据操作失败
    DATABASE_OPERATION_ERROR(500002, "数据库操作失败"),
    // 获取系统信息异常
    SYSTEM_MONITOR_ERROR(500003, "获取系统信息异常"),


    //#################### 600 业务类异常 ####################//
    SAVE_ARTICLE_ERROR(600001, "保存文章失败"),
    LOGIN_VERIFY_CODE_EXPIRED_ERROR(600002, "验证码已过期，请刷新验证码后再试"),
    EMAIL_OCCUPY_BY_OTHER_USER_ERROR(600003, "邮箱被其他用户占用"),
    ROLE_NAME_EXIST_ERROR_MESSAGE(600004, "角色名已存在"),
    ROLE_IS_RELATED_BY_USER_ERROR_MESSAGE(600005, "角色关联了用户，无法删除"),
    NAME_OCCUPY_BY_OTHER_ROLE_ERROR_MESSAGE(600006, "名称被其他角色占用"),
    ROUTE_NAME_EXIST_ERROR_MESSAGE(600007, "菜单路由名称已存在"),
    ROUTE_PATH_EXIST_ERROR_MESSAGE(600008, "菜单路由路径已存在"),
    MENU_PERMISSION_EXIST_ERROR_MESSAGE(600009, "菜单权限已存在"),

    //#################### 未知异常信息 ####################//
    UNKNOWN_ERROR(-1, "未知系统异常"),

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
