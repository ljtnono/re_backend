package cn.lingjiatong.re.common;

import cn.lingjiatong.re.common.exception.ErrorEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 通用返回VO对象
 *
 * @author Ling, Jiatong
 * Date: 2021/8/10 12:25 上午
 */
@Data
@ApiModel(description = "通用返回VO对象")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultVO <T> {

    /**
     * 操作成功返回码
     */
    public static final Integer CODE_SUCCESS = 0;

    /**
     * 操作成功返回消息
     */
    public static final String MESSAGE_SUCCESS = "success";

    /**
     * 返回码
     */
    private Integer code;

    /**
     * 消息
     */
    private String message;

    /**
     * 数据
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    private ResultVO() {}

    /**
     * 私有构造方法 供失败响应调用
     *
     * @param code    响应码
     * @param message 错误信息
     */
    private ResultVO(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 返回成功消息
     *
     * @param t 数据
     * @param <T> 泛型
     * @author Ling, Jiatong
     */
    public static <T> ResultVO<T> success(T t) {
        ResultVO<T> vo = new ResultVO<T>();
        vo.setCode(CODE_SUCCESS);
        vo.setMessage(MESSAGE_SUCCESS);
        vo.setData(t);
        return vo;
    }

    /**
     * 返回成功消息（没有数据体）
     *
     * @return 通用消息返回对象
     * @author Ling, Jiatong
     */
    public static ResultVO<?> success() {
        ResultVO<?> vo = new ResultVO<>();
        vo.setCode(CODE_SUCCESS);
        vo.setMessage(MESSAGE_SUCCESS);
        return vo;
    }

    /**
     * 失败响应 传入code和message
     *
     * @param code    响应码
     * @param message 错误信息
     * @param <T>     范型
     * @return 统一返回值封装
     */
    public static <T> ResultVO<T> error(Integer code, String message) {
        return new ResultVO<>(code, message);
    }

    /**
     * 失败响应 传入错误枚举
     *
     * @param errorEnum HTTP Restful Api 错误枚举
     * @param <T>       范型
     * @return 统一返回值封装
     */
    public static <T> ResultVO<T> error(ErrorEnum errorEnum) {
        return new ResultVO<>(errorEnum.getCode(), errorEnum.getMessage());
    }

}
