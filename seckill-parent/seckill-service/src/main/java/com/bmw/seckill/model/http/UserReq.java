package com.bmw.seckill.model.http;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class UserReq implements Serializable {

    @Data
    public static class BaseUserInfo implements  Serializable {
        @NotNull(message = "手机号不能为空")
        private String phone;
    }

    @Data
    public static class LoginUserInfo extends   BaseUserInfo {
        @NotNull(message = "短信验证码不能为空")
        private String smsCode;
    }

}
