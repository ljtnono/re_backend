package cn.lingjiatong.re.common.aop;

import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.common.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * 全局异常处理
 *
 * @author Ling, Jiatong
 * Date: 2022/9/19 17:05
 */
@Slf4j
@RestControllerAdvice
public class GlobalControllerAspect {

    /**
     * 处理请求参数错误异常
     *
     * @param e 请求参数错误异常
     * @return 通用消息返回对象
     */
    @ExceptionHandler({ParamErrorException.class, MethodArgumentTypeMismatchException.class})
    @ResponseStatus(value = HttpStatus.OK)
    public ResultVO<?> handleParamErrorException(ParamErrorException e) {
        log.error("==========请求参数错误异常");
        log.error(e.toString(), e);
        return ResultVO.error(e.getCode(), e.getMessage());
    }

    /**
     * 处理资源已存在异常
     *
     * @param e 资源已存在异常
     * @return 通用消息返回对象
     */
    @ExceptionHandler(ResourceAlreadyExistException.class)
    @ResponseStatus(value = HttpStatus.OK)
    public ResultVO<?> handleResourceAlreadyExistException(ResourceAlreadyExistException e) {
        log.error("==========资源已存在异常");
        log.error(e.toString(), e);
        return ResultVO.error(e.getCode(), e.getMessage());
    }

    /**
     * 处理业务异常
     *
     * @param e 业务异常
     * @return 通用消息返回对象
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(value = HttpStatus.OK)
    public ResultVO<?> handleBusinessException(BusinessException e) {
        log.error("==========业务异常");
        log.error(e.toString(), e);
        return ResultVO.error(e.getCode(), e.getMessage());
    }

    /**
     * 服务器异常
     *
     * @param e 服务器异常
     * @return 通用消息返回对象
     */
    @ExceptionHandler(ServerException.class)
    @ResponseStatus(value = HttpStatus.OK)
    public ResultVO<?> handleServerException(ServerException e) {
        log.error("==========服务器异常");
        log.error(e.toString(), e);
        return ResultVO.error(e.getCode(), e.getMessage());
    }


    /**
     * 处理资源不存在异常
     *
     * @param e 资源不存在异常
     * @return 通用消息返回对象
     */
    @ExceptionHandler(ResourceNotExistException.class)
    @ResponseStatus(value = HttpStatus.OK)
    public ResultVO<?> handleResourceNotExistException(ResourceNotExistException e) {
        log.error("==========资源不存在异常");
        log.error(e.toString(), e);
        return ResultVO.error(e.getCode(), e.getMessage());
    }

    /**
     * 处理controller参数缺失异常
     *
     * @param e 资源不存在异常
     * @return 通用消息返回对象
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(value = HttpStatus.OK)
    public ResultVO<?> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        log.error("==========参数缺失");
        log.error(e.toString(), e);
        return ResultVO.error(ErrorEnum.REQUEST_PARAM_ERROR);
    }

//    /**
//     * 禁止访问异常
//     *
//     * @param e 禁止访问异常
//     * @return 通用消息返回对象
//     * @author Ling, Jiatong
//     */
//    @ExceptionHandler(AccessDeniedException.class)
//    @ResponseStatus(HttpStatus.OK)
//    public ResultVO<?> handleAccessDeniedException(AccessDeniedException e) {
//        log.error("==========访问权限异常");
//        String message = "禁止访问";
//        if (e != null) {
//            log.error(e.toString(), e);
//        }
//        return ResultVO.error(403, message);
//    }

    /**
     * 未知系统异常
     *
     * @param e 未知系统异常
     * @return 通用消息返回对象
     * @author Ling, Jiatong
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    public ResultVO<?> handleException(Exception e) {
        log.error("==========未知系统异常");
        String message = "操作失败，未知系统异常";
        if (e != null) {
            log.error(e.toString(), e);
        }
        return ResultVO.error(500003, message);
    }
}
