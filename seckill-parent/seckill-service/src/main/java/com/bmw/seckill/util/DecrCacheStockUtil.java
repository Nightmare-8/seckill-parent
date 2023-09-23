package com.bmw.seckill.util;

import com.bmw.seckill.common.base.Constant;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Slf4j
public class DecrCacheStockUtil {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private DefaultRedisScript<Long> getRedisScript;


    private String subStock = "local key=KEYS[1];\n" +
            "local surplusStock = tonumber(redis.call('get',key));\n" +
            "if (surplusStock<=0) then return 0\n" +
            "else\n" +
            "    redis.call('incrby', key, -1)\n" +
            "    return 2 \n" +
            "end";

    // Constructor >> @Autowired >> @PostConstruct
    // @PostConstruct注解的方法在项目启动的时候执行这个方法，也可以理解为在spring容器启动的时候执行，可作为一些数据的常规化加载，比如数据字典之类的
    @PostConstruct
    public void init() {
        getRedisScript = new DefaultRedisScript<Long>();
        getRedisScript.setResultType(Long.class);
        getRedisScript.setScriptText(subStock);
    }

    /**
     * 减库存
     *
     * @param productId
     * @return
     */
    public Long decrStock(Long productId) {
        Long res = redisTemplate.execute(getRedisScript, Lists.newArrayList(String.format(Constant.redisKey.SECKILL_SALED_COUNT, productId)));
        return res;
    }

    /**
     * 加库存
     *
     * @param productId
     * @return
     */
    public void addStock(Long productId) {
        String key = String.format(Constant.redisKey.SECKILL_SALED_COUNT, productId);
        if (redisTemplate.hasKey(key)) {
            redisTemplate.opsForValue().increment(key);
        }
    }
}
