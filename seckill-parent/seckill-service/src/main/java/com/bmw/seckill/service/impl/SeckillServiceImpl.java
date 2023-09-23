package com.bmw.seckill.service.impl;

import com.bmw.seckill.common.base.BaseResponse;
import com.bmw.seckill.common.base.Constant;
import com.bmw.seckill.common.exception.ErrorMessage;
import com.bmw.seckill.dao.SeckillOrderDao;
import com.bmw.seckill.dao.SeckillProductsDao;
import com.bmw.seckill.dao.SeckillUserDao;
import com.bmw.seckill.model.SeckillOrder;
import com.bmw.seckill.model.SeckillProducts;
import com.bmw.seckill.model.http.SeckillReq;
import com.bmw.seckill.service.SeckillService;
import com.bmw.seckill.util.DecrCacheStockUtil;
import com.bmw.seckill.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;


@Service
@Slf4j
public class SeckillServiceImpl implements SeckillService {


    @Autowired
    DecrCacheStockUtil decrCacheStockUtil;

    @Autowired
    private SeckillOrderDao seckillOrderDao;

    @Autowired
    private SeckillProductsDao seckillProductsDao;

    @Autowired
    private SeckillUserDao seckillUserDao;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    private Redisson redisson;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse sOrder(SeckillReq req) {
        log.info("===[开始调用原始下单接口~]===");
        //参数校验
        log.info("===[校验用户信息及商品信息]===");
        BaseResponse paramValidRes = validateParam(req.getProductId(), req.getUserId());
        if (paramValidRes.getCode() != 0) {
            return paramValidRes;
        }
        log.info("===[校验参数是否合法][通过]===");

        log.info("===[校验 用户是否重复下单]===");
        SeckillOrder param = new SeckillOrder();
        param.setProductId(req.getProductId());
        param.setUserId(req.getUserId());
        int repeatCount = seckillOrderDao.count(param);
        if (repeatCount > 0) {
            log.error("===[该用户重复下单！]===");
            return BaseResponse.error(ErrorMessage.REPEAT_ORDER_ERROR);
        }
        log.info("===[校验 用户是否重复下单][通过校验]===");


        Long productId = req.getProductId();
        Long userId = req.getUserId();
        SeckillProducts product = seckillProductsDao.selectByPrimaryKey(productId);
        Date date = new Date();
        // 扣减库存
        log.info("===[开始扣减库存]===");
        product.setSaled(product.getSaled() + 1);
        seckillProductsDao.updateByPrimaryKeySelective(product);
        log.info("===[扣减库存][成功]===");
        // 创建订单
        log.info("===[开始创建订单]===");
        SeckillOrder order = new SeckillOrder();
        order.setProductId(productId);
        order.setProductName(product.getName());
        order.setUserId(userId);
        order.setCreateTime(date);
        seckillOrderDao.insert(order);
        log.info("===[创建订单][成功]===");
        return BaseResponse.OK;
    }

    /**
     * 悲观锁实现方式
     *
     * @param req
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse pOrder(SeckillReq req) {
        log.info("===[开始调用秒杀接口(悲观锁)]===");
        //校验用户信息、商品信息、库存信息
        log.info("===[校验用户信息、商品信息、库存信息]===");
        BaseResponse paramValidRes = validateParamPessimistic(req.getProductId(), req.getUserId());
        if (paramValidRes.getCode() != 0) {
            return paramValidRes;
        }
        log.info("===[校验][通过]===");

        log.info("===[校验 用户是否重复下单]===");
        SeckillOrder param = new SeckillOrder();
        param.setProductId(req.getProductId());
        param.setUserId(req.getUserId());
        int repeatCount = seckillOrderDao.count(param);
        if (repeatCount > 0) {
            log.error("===[该用户重复下单！]===");
            return BaseResponse.error(ErrorMessage.REPEAT_ORDER_ERROR);
        }
        log.info("===[校验 用户是否重复下单][通过校验]===");

        Long userId = req.getUserId();
        Long productId = req.getProductId();
        SeckillProducts product = seckillProductsDao.selectByPrimaryKey(productId);
        // 下单逻辑
        log.info("===[开始下单逻辑]===");
        Date date = new Date();
        // 扣减库存
        product.setSaled(product.getSaled() + 1);
        seckillProductsDao.updateByPrimaryKeySelective(product);

        // 创建订单
        SeckillOrder order = new SeckillOrder();
        order.setProductId(productId);
        order.setProductName(product.getName());
        order.setUserId(userId);
        order.setCreateTime(date);
        seckillOrderDao.insert(order);
        return BaseResponse.OK;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse oOrder(SeckillReq req) throws Exception {
        log.info("===[开始调用下单接口~（乐观锁）]===");
        //校验用户信息、商品信息、库存信息
        log.info("===[校验用户信息、商品信息、库存信息]===");
        BaseResponse paramValidRes = validateParam(req.getProductId(), req.getUserId());
        if (paramValidRes.getCode() != 0) {
            return paramValidRes;
        }
        log.info("===[校验][通过]===");

        //下单（乐观锁）
        return createOptimisticOrder(req.getProductId(), req.getUserId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse redissonOrder(SeckillReq req) {
        log.info("===[开始调用下单接口（redission）~]===");
        String lockKey = String.format(Constant.redisKey.SECKILL_DISTRIBUTED_LOCK, req.getProductId());
        RLock lock = redisson.getLock(lockKey);
        try {
            //将锁过期时间设置为30s，定时任务每隔10秒执行续锁操作
            lock.lock();

            //** 另一种写法 **
            //先拿锁，在设置超时时间，看门狗就不会自动续期，锁到达过期时间后，就释放了
            //lock.lock(30, TimeUnit.SECONDS);
            //参数校验
            log.info("===[校验用户信息及商品信息]===");
            BaseResponse paramValidRes = validateParam(req.getProductId(), req.getUserId());
            if (paramValidRes.getCode() != 0) {
                return paramValidRes;
            }
            log.info("===[校验参数是否合法][通过]===");

            Long productId = req.getProductId();
            Long userId = req.getUserId();
            SeckillProducts product = seckillProductsDao.selectByPrimaryKey(productId);
            Date date = new Date();
            // 扣减库存
            log.info("===[开始扣减库存]===");
            product.setSaled(product.getSaled() + 1);
            seckillProductsDao.updateByPrimaryKeySelective(product);
            log.info("===[扣减库存][成功]===");
            // 创建订单
            log.info("===[开始创建订单]===");
            SeckillOrder order = new SeckillOrder();
            order.setProductId(productId);
            order.setProductName(product.getName());
            order.setUserId(userId);
            order.setCreateTime(date);
            seckillOrderDao.insert(order);
            log.info("===[创建订单][成功]===");
            return BaseResponse.OK;
        } catch (Exception e) {
            log.error("===[异常！]===", e);
            return BaseResponse.error(ErrorMessage.SYS_ERROR);
        } finally {
            lock.unlock(); // 释放锁
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse cOrder(SeckillReq req) throws Exception {
        log.info("===[开始调用下单接口~（避免超卖——Redis）]===");
        long res = 0;
        try {
            //校验用户信息、商品信息、库存信息
            log.info("===[校验用户信息、商品信息、库存信息]===");
            BaseResponse paramValidRes = validateParam(req.getProductId(), req.getUserId());
            if (paramValidRes.getCode() != 0) {
                return paramValidRes;
            }
            log.info("===[校验][通过]===");

            Long productId = req.getProductId();
            Long userId = req.getUserId();

            //redis + lua
            res = decrCacheStockUtil.decrStock(req.getProductId());
            if (res == 2) {
                // 扣减完的库存只要大于等于0，就说明扣减成功
                // 开始数据库扣减库存逻辑
                seckillProductsDao.decrStock(productId);
                // 创建订单
                SeckillProducts product = seckillProductsDao.selectByPrimaryKey(productId);
                Date date = new Date();
                SeckillOrder order = new SeckillOrder();
                order.setProductId(productId);
                order.setProductName(product.getName());
                order.setUserId(userId);
                order.setCreateTime(date);
                seckillOrderDao.insert(order);
                return BaseResponse.OK;
            } else {
                log.error("===[缓存扣减库存不足！]===");
                return BaseResponse.error(ErrorMessage.STOCK_NOT_ENOUGH);
            }
        } catch (Exception e) {
            log.error("===[异常！]===", e);
            if (res == 2) {
                decrCacheStockUtil.addStock(req.getProductId());
            }
            throw new Exception("异常！");
        }

    }

    private BaseResponse createOptimisticOrder(Long productId, Long userId) throws Exception {
        log.info("===[下单逻辑Starting]===");
        // 创建订单
        SeckillProducts products = seckillProductsDao.selectByPrimaryKey(productId);
        Date date = new Date();
        SeckillOrder order = new SeckillOrder();
        order.setProductId(productId);
        order.setProductName(products.getName());
        order.setUserId(userId);
        order.setCreateTime(date);
        seckillOrderDao.insert(order);
        log.info("===[创建订单成功]===");

        //扣减库存
        int res = seckillProductsDao.updateStockByOptimistic(productId);
        if (res == 0) {
            log.error("===[秒杀失败，抛出异常，执行回滚逻辑！]===");
            throw new Exception("库存不足");
//          return BaseResponse.error(ErrorMessage.SECKILL_FAILED);
        }
        log.info("===[扣减库存成功!]===");
        try {
            addOrderedUserCache(productId, userId);
        } catch (Exception e) {
            log.error("===[记录已购用户缓存时发生异常！]===", e);
        }
        return BaseResponse.OK;
    }

    public void addOrderedUserCache(Long productId, Long userId) {
        String key = String.format(Constant.redisKey.SECKILL_ORDERED_USER, productId);
        redisUtil.sSet(key, userId);
        log.info("===[已将已购用户放入缓存！]===");
    }

    public Boolean hasOrderedUserCache(Long productId, Long userId) {
        String key = String.format(Constant.redisKey.SECKILL_ORDERED_USER, productId);
        return redisUtil.sHasKey(key, userId);
    }


    private BaseResponse validateParam(Long productId, Long userId) {
        SeckillProducts product = seckillProductsDao.selectByPrimaryKey(productId);
        if (product == null) {
            log.error("===[产品不存在！]===");
            return BaseResponse.error(ErrorMessage.SYS_ERROR);
        }

        if (product.getStartBuyTime().getTime() > System.currentTimeMillis()) {
            log.error("===[秒杀还未开始！]===");
            return BaseResponse.error(ErrorMessage.SECKILL_NOT_START);
        }

        if (product.getSaled() >= product.getCount()) {
            log.error("===[库存不足！]===");
            return BaseResponse.error(ErrorMessage.STOCK_NOT_ENOUGH);
        }
        if (hasOrderedUserCache(productId, userId)) {
            log.error("===[用户重复下单！]===");
            return BaseResponse.error(ErrorMessage.REPEAT_ORDER_ERROR);
        }
        return BaseResponse.OK;
    }

    private BaseResponse validateParamPessimistic(Long productId, Long userId) {
        //悲观锁，利用selectForUpdate方法锁定记录，并获得最新的SeckillProducts记录
        SeckillProducts product = seckillProductsDao.selectForUpdate(productId);
        if (product == null) {
            log.error("===[产品不存在！]===");
            return BaseResponse.error(ErrorMessage.SYS_ERROR);
        }

        if (product.getStartBuyTime().getTime() > System.currentTimeMillis()) {
            log.error("===[秒杀还未开始！]===");
            return BaseResponse.error(ErrorMessage.SECKILL_NOT_START);
        }

        if (product.getSaled() >= product.getCount()) {
            log.error("===[库存不足！]===");
            return BaseResponse.error(ErrorMessage.STOCK_NOT_ENOUGH);
        }
        return BaseResponse.OK;
    }


}
