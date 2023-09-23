package com.bmw.seckill;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@MapperScan(value = "com.bmw.seckill.dao")
public class SeckillAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(SeckillAdminApplication.class, args);
    }
}
