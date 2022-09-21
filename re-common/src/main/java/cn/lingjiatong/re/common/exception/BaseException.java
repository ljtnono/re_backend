package cn.lingjiatong.re.common.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 异常基类
 *
 * @author Ling, Jiatong
 * Date: 2021/8/31 11:46 下午
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BaseException extends RuntimeException {
    private Integer code;


    public BaseException(ErrorEnum errorEnum) {
        super(errorEnum.getMessage());
        this.code = errorEnum.getCode();
    }


    public BaseException(Integer code, String message) {
        super(message);
        this.code = code;
    }

}
