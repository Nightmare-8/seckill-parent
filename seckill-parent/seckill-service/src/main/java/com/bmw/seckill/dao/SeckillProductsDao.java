package com.bmw.seckill.dao;

import com.bmw.seckill.common.util.bean.CommonQueryBean;
import com.bmw.seckill.model.SeckillProducts;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * SeckillProducts数据库操作接口类
 **/

@Repository
public interface SeckillProductsDao {


    /**
     * 查询（根据主键ID查询）
     **/
    SeckillProducts selectByPrimaryKey(@Param("id") Long id);

    /**
     * 删除（根据主键ID删除）
     **/
    int deleteByPrimaryKey(@Param("id") Long id);

    /**
     * 添加
     **/
    int insert(SeckillProducts record);

    /**
     * 修改 （匹配有值的字段）
     **/
    int updateByPrimaryKeySelective(SeckillProducts record);

    /**
     * list分页查询
     **/
    List<SeckillProducts> list4Page(SeckillProducts record, @Param("commonQueryParam") CommonQueryBean query);

    /**
     * count查询
     **/
    int count(SeckillProducts record);

    /**
     * list查询
     **/
    List<SeckillProducts> list(SeckillProducts record);

    /**
     * 根据productId锁定记录
     */
    SeckillProducts selectForUpdate(Long id);

    /**
     * 乐观锁的方式扣减库存
     *
     * @param productId
     * @return
     */
    int updateStockByOptimistic(Long productId);

    /**
     * 扣减库存
     *
     * @param productId
     * @return
     */
    int decrStock(Long productId);
}