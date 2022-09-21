package cn.lingjiatong.re.common.exception;

/**
 * 请求参数错误异常
 *
 * @author Ling, Jiatong
 * Date: 2022/9/19 16:56
 */
public class ParamErrorException extends BaseException{

    public ParamErrorException(ErrorEnum errorEnum) {
        super(errorEnum);
    }

    public ParamErrorException(Integer code, String message) {
        super(code, message);
    }
}
