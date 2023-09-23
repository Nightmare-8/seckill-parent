package com.bmw.seckill.service.impl;

import cn.hutool.core.lang.Assert;
import com.bmw.seckill.dao.SeckillUserDao;
import com.bmw.seckill.model.SeckillUser;
import com.bmw.seckill.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service("userService")
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private SeckillUserDao seckillUserDao;

    @Override
    public SeckillUser findByPhone(String phone) {
        Assert.notNull(phone);

        SeckillUser seckillUser = new SeckillUser();
        seckillUser.setPhone(phone);
        List<SeckillUser> list = seckillUserDao.list(seckillUser);
        if (!CollectionUtils.isEmpty(list)) {
            Assert.isTrue(list.size() == 1);
            return list.get(0);
        }

        return null;
    }
}
