package cn.lingjiatong.re.common.exception;

/**
 * 业务异常，主要是指在请求过程中业务流程上出现的异常
 *
 * @author Ling, Jiatong
 * Date: 2022/10/19 22:02
 */
public class BusinessException extends BaseException {

    public BusinessException(ErrorEnum errorEnum) {
        super(errorEnum);
    }

    public BusinessException(Integer code, String message) {
        super(code, message);
    }
}
