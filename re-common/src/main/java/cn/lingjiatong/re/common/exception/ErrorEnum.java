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


    /** 500 Bad Request */
    // MINIO服务器出现异常
    MINIO_SERVER_ERROR(500001, "图片服务器异常，上传失败"),



    /** 600 业务类异常 */
    SAVE_ARTICLE_ERROR(600001, "保存文章失败")

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
