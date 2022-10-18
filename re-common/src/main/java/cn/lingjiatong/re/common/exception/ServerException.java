package cn.lingjiatong.re.common.exception;

/**
 * 服务器异常
 *
 * @author Ling, Jiatong
 * Date: 2022/10/18 20:39
 */
public class ServerException extends BaseException {

    public ServerException(ErrorEnum errorEnum) {
        super(errorEnum);
    }

    public ServerException(Integer code, String message) {
        super(code, message);
    }
}
