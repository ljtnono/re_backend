package cn.lingjiatong.re.common.exception;

/**
 * 权限相关异常
 *
 * @author Ling, Jiatong
 * Date: 2022/10/23 10:39
 */
public class PermissionException extends BaseException {

    public PermissionException(ErrorEnum errorEnum) {
        super(errorEnum);
    }

    public PermissionException(Integer code, String message) {
        super(code, message);
    }
}
