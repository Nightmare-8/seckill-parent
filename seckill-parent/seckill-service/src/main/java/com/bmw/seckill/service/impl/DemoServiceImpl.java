package com.bmw.seckill.service.impl;

import com.bmw.seckill.dao.DemoDao;
import com.bmw.seckill.model.Demo;
import com.bmw.seckill.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DemoServiceImpl implements DemoService {

    @Autowired
    private DemoDao demoDao;

    @Override
    public List<Demo> list() {
        return demoDao.list(new Demo());
    }
}
