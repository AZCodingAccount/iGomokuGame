package com.gobanggame.exception;

/**
 * @program: gobanggame
 * @author: AlbertZhang
 * @create: 2023-12-11 12:22
 * @description: 自定义异常的基类
 **/
public class BaseException extends RuntimeException {
    public BaseException() {
    }

    public BaseException(String msg) {
        super(msg);
    }

}
