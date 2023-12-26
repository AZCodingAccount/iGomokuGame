package com.gobanggame.annotation;

import com.gobanggame.constant.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @program: gobanggame
 * @author: AlbertZhang
 * @create: 2023-12-09 21:46
 * @description: 自定义注解，公共字段自动填充
 **/

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoFill {
    OperationType value();
}

