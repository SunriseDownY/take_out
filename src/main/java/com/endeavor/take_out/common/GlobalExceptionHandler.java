package com.endeavor.take_out.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @author Sunrise
 * @create 2022-09-30 15:41
 * <p>
 * 全局异常处理
 */
@Slf4j
@RestControllerAdvice(annotations = {RestController.class, Controller.class})
public class GlobalExceptionHandler {
    /**
     * 处理员工异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex) {
        String errorMessage = ex.getMessage();
        log.error(errorMessage);
        if (errorMessage.contains("Duplicate entry")) {
            String[] msg = errorMessage.split(" ");
            return R.error("该用户" + msg[2] + "已存在");
        }
        return R.error("未知错误");
    }

    /**
     * 处理分类异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(CustomException.class)
    public R<String> exceptionHandler(CustomException ex) {
        String errorMessage = ex.getMessage();
        log.error(errorMessage);
        return R.error(errorMessage);
    }
}
