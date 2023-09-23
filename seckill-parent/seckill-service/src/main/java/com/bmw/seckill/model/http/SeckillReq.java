package com.bmw.seckill.model.http;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author han
 * @version 1.0
 * @date 2020/07/29
 */
@Data
public class SeckillReq implements Serializable {

    @NotNull(message = "产品id 不能为空")
    private Long productId;

    private Long userId;
}
