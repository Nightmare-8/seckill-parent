package com.bmw.seckill.service;

import com.bmw.seckill.model.SeckillUser;

public interface UserService {

    SeckillUser findByPhone(String phone);
}
