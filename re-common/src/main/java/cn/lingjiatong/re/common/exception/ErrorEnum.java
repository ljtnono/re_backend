package cn.lingjiatong.re.common.exception;

/**
 * 错误消息枚举类
 *
 * @author Ling, Jiatong
 * Date: 2021/9/5 1:07 上午
 */
public enum ErrorEnum {


    /** 400 Bad Request */

    /**
     * 请求参数为空 ｜ 请求参数格式有误
     */
    REQUEST_PARAM_ERROR(400001, "请求参数有误"),


    /** 500 Bad Request */

    MINIO_SERVER_ERROR(500001, "图片服务器异常，上传失败"),

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
