package com.craft.frame.boot.web.exception;

import com.craft.frame.boot.web.response.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * @author DURR
 * @desc 全局异常处理
 * @date 2023/6/25 20:43
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * validation参数校验异常
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Operation<?> methodArgumentNotValidExceptionExceptionHandler(MethodArgumentNotValidException e) {
        StringBuilder errorMsg = new StringBuilder();
        e.getBindingResult().getFieldErrors().forEach(x -> errorMsg.append(x.getField()).append(x.getDefaultMessage()).append(","));
        String message = errorMsg.toString();
        LOGGER.info("validation parameters error！The reason is:{}", message);
        return Operation.fail(-1, message.substring(0, message.length() - 1));
    }

    /**
     * 处理空指针的异常
     */
    @ExceptionHandler(value = NullPointerException.class)
    public Operation<?> exceptionHandler(NullPointerException e) {
        LOGGER.error("null point exception！The reason is:{}", e.getMessage(), e);
        return Operation.fail(CommonErrorEnum.SYSTEM_ERROR.getErrorCode(), CommonErrorEnum.SYSTEM_ERROR.getErrorMsg());
    }

    /**
     * 未知异常
     */
    @ExceptionHandler(value = Exception.class)
    public Operation<?> systemExceptionHandler(Exception e) {
        LOGGER.error("system exception！The reason is：{}", e.getMessage(), e);
        return Operation.fail(CommonErrorEnum.SYSTEM_ERROR.getErrorCode(), CommonErrorEnum.SYSTEM_ERROR.getErrorMsg());
    }

    /**
     * 业务异常
     */
    @ExceptionHandler(value = BusinessException.class)
    public Operation<?> businessExceptionHandler(BusinessException e) {
        LOGGER.info("business exception！The reason is：{}", e.getMessage(), e);
        return Operation.fail(e.getErrorCode(), e.getMessage());
    }

    /**
     * http请求方式不支持
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Operation<Void> handleException(HttpRequestMethodNotSupportedException e) {
        LOGGER.error(e.getMessage(), e);
        return Operation.fail(-1, String.format("不支持'%s'请求", e.getMethod()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public Operation<Void> handleException(AccessDeniedException e) {
        LOGGER.error(e.getMessage(), e);
        return Operation.fail(CommonErrorEnum.NO_ACCESS.getErrorCode(), CommonErrorEnum.NO_ACCESS.getErrorMsg());
    }

}
