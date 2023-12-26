package com.gobanggame.pojo.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @program: gobanggame
 * @author: AlbertZhang
 * @create: 2023-12-09 20:53
 * @description: 返回的封装结果类
 **/
@Data
@Schema(description = "响应结果")
public class Result<T> implements Serializable {
    @Schema(description = "编码(1成功，其他数字失败)")
    private Integer code; // 编码：1成功，0和其它数字为失败
    @Schema(description = "错误信息（正确信息为succes）")
    private String msg; // 错误信息
    @Schema(description = "响应数据")
    private T data; // 数据

    public static <T> Result<T> success() {
        Result<T> result = new Result<T>();
        result.code = 1;
        result.msg = "success";
        return result;
    }

    public static <T> Result<T> success(T object) {
        Result<T> result = new Result<T>();
        result.data = object;
        result.msg = "success";
        result.code = 1;
        return result;
    }

    public static <T> Result<T> error(String msg) {
        Result result = new Result();
        result.msg = msg;
        result.code = 0;
        return result;
    }

}
