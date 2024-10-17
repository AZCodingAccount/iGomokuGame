package com.gobanggame.exception;

import com.gobanggame.pojo.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @program: gobanggame
 * @author: AlbertZhang
 * @create: 2023-12-09 23:47
 * @description: 全局异常处理器
 **/
@RestControllerAdvice
@Slf4j
public class GlobalException {
    /**
     * 全局异常处理
     *
     * @param e
     * @return
     */
    @ExceptionHandler
    public Result<Object> exceptionHandler(Exception e) {
        log.error("异常信息：{}", e.getMessage());
        // 捕获我们自定义的那些异常
        if (e instanceof BaseException) {
            return Result.error(e.getMessage());
        }
        // 如果没有自定义的异常的异常
        return Result.error("未知异常，请联系管理员");
    }
}
