package cn.lingjiatong.re.common.exception;

/**
 * 资源已存在异常
 *
 * @author Ling, Jiatong
 * Date: 2022/9/17 19:07
 */
public class ResourceAlreadyExistException extends BaseException{

    public ResourceAlreadyExistException(ErrorEnum errorEnum) {
        super(errorEnum);
    }

    public ResourceAlreadyExistException(Integer code, String message) {
        super(code, message);
    }
}
