package com.bmw.seckill.service;

import com.bmw.seckill.common.base.BaseResponse;
import com.bmw.seckill.model.http.SeckillReq;

public interface SeckillService {

    BaseResponse sOrder(SeckillReq req);

    BaseResponse pOrder(SeckillReq req);

    BaseResponse oOrder(SeckillReq req) throws Exception;

    BaseResponse redissonOrder(SeckillReq req);

    BaseResponse cOrder(SeckillReq req) throws Exception;
}
