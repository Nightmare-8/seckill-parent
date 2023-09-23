package com.bmw.seckill.common.base;

import com.bmw.seckill.common.exception.ErrorMessage;

import java.io.Serializable;

/**
 * @author bobo
 * @Description:
 * @date 2019-03-11 11:47
 */
public class BaseResponse<T> implements Serializable {
    public static final BaseResponse OK = new BaseResponse(0, "成功", "");
    private Integer code;
    private String message;
    private T data;

    public BaseResponse(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static BaseResponse ok(Object o) {
        return new BaseResponse(0, "成功", o);
    }

    public static <T> BaseResponse<T> OK(T t) {
        return new BaseResponse(0, "成功", t);
    }

    public static BaseResponse error(ErrorMessage errorMessage) {
        return new BaseResponse(errorMessage.getCode(), errorMessage.getMessage(), null);
    }

    public static <T> BaseResponse<T> error(ErrorMessage errorMessage, T data) {
        return new BaseResponse(errorMessage.getCode(), errorMessage.getMessage(), data);
    }

    public static BaseResponse error(Integer code, String message) {
        return new BaseResponse(code, message, null);
    }

    public static <T> BaseResponse<T> error(Integer code, String message, T data) {
        return new BaseResponse(code, message, data);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
