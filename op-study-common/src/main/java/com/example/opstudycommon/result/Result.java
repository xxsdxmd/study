package com.example.opstudycommon.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xxs
 * @Date 2024/6/28 22:12
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {

    private Integer code;
    private String msg;
    private T data;

    // 成功状态码
    public static final Integer SUCCESS_CODE = 200;
    // 失败状态码
    public static final Integer ERROR_CODE = 500;
    // 参数错误状态码
    public static final Integer PARAM_ERROR_CODE = 400;

    /**
     * 成功返回，无数据
     */
    public static <T> Result<T> success() {
        return Result.<T>builder()
                .code(SUCCESS_CODE)
                .msg("成功")
                .build();
    }

    /**
     * 成功返回，带数据
     */
    public static <T> Result<T> success(T data) {
        return Result.<T>builder()
                .code(SUCCESS_CODE)
                .msg("成功")
                .data(data)
                .build();
    }

    /**
     * 成功返回，自定义消息
     */
    public static <T> Result<T> success(String msg, T data) {
        return Result.<T>builder()
                .code(SUCCESS_CODE)
                .msg(msg)
                .data(data)
                .build();
    }

    /**
     * 失败返回
     */
    public static <T> Result<T> error(String msg) {
        return Result.<T>builder()
                .code(ERROR_CODE)
                .msg(msg)
                .build();
    }

    /**
     * 失败返回，自定义状态码
     */
    public static <T> Result<T> error(Integer code, String msg) {
        return Result.<T>builder()
                .code(code)
                .msg(msg)
                .build();
    }

    /**
     * 参数错误
     */
    public static <T> Result<T> paramError(String msg) {
        return Result.<T>builder()
                .code(PARAM_ERROR_CODE)
                .msg(msg)
                .build();
    }

    /**
     * 判断是否成功
     */
    public boolean isSuccess() {
        return SUCCESS_CODE.equals(this.code);
    }

    /**
     * 判断是否失败
     */
    public boolean isError() {
        return !isSuccess();
    }
}
