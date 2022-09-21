package cn.lingjiatong.re.common.exception;

/**
 * 资源不存在异常
 *
 * @author Ling, Jiatong
 * Date: 2021/8/31 11:45 下午
 */
public class ResourceNotExistException extends BaseException{

    public ResourceNotExistException(ErrorEnum errorEnum) {
        super(errorEnum);
    }

    public ResourceNotExistException(Integer code, String message) {
        super(code, message);
    }
}
