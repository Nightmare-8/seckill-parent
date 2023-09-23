package com.bmw.seckill.common.exception;

/**
 * @author bobo
 * @date 2019-03-11 15:46
 */
public enum ErrorMessage {

    /**
     * 通用ERROR
     */
    SYS_ERROR(10000, "系统开小差了,稍后再试"),
    PARAM_ERROR(10001, "参数错误"),
    LOGIN_ERROR(10002, "用户未登录"),


    /**
     * 秒杀ERROR
     */
    STOCK_NOT_ENOUGH(20001, "库存不足！"),
    REPEAT_ORDER_ERROR(20002, "不能重复下单！"),
    SECKILL_NOT_START(20003, "秒杀活动还没开始！"),
    SECKILL_FAILED(20004, "秒杀失败！！"),

    /**
     * user相关error.
     */
    SMSCODE_ERROR(30001, "短信验证码错误"),
    PHONE_EXIST(30002, "手机号已经存在");


    private Integer code;
    private String message;

    ErrorMessage(Integer code, String message) {
        this.code = code;
        this.message = message;
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

    public ErrorMessage setMessage(String message) {
        this.message = message;
        return this;
    }
}
