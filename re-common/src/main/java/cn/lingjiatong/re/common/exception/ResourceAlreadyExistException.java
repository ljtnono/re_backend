package cn.lingjiatong.re.common.exception;

/**
 * 资源已存在异常
 *
 * @author Ling, Jiatong
 * Date: 2022/9/17 19:07
 */
public class ResourceAlreadyException extends BaseException{

    public ResourceAlreadyException(ErrorEnum errorEnum) {
        super(errorEnum);
    }

    public ResourceAlreadyException(Integer code, String message) {
        super(code, message);
    }
}
