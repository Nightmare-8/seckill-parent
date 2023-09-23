package com.bmw.seckill.model.http;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Data
public class UserResp implements Serializable {

    @Data
    public static class BaseUserResp implements  Serializable {

        private String token;
    }

}
