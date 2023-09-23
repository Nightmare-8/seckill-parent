package com.bmw.seckill.dao;

import com.bmw.seckill.common.util.bean.CommonQueryBean;
import com.bmw.seckill.model.SeckillAdmin;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * SeckillAdmin数据库操作接口类
 **/

@Repository
public interface SeckillAdminDao {


    /**
     * 查询（根据主键ID查询）
     **/
    SeckillAdmin selectByPrimaryKey(@Param("id") Long id);

    /**
     * 删除（根据主键ID删除）
     **/
    int deleteByPrimaryKey(@Param("id") Long id);

    /**
     * 添加
     **/
    int insert(SeckillAdmin record);

    /**
     * 修改 （匹配有值的字段）
     **/
    int updateByPrimaryKeySelective(SeckillAdmin record);

    /**
     * list分页查询
     **/
    List<SeckillAdmin> list4Page(SeckillAdmin record, @Param("commonQueryParam") CommonQueryBean query);

    /**
     * count查询
     **/
    int count(SeckillAdmin record);

    /**
     * list查询
     **/
    List<SeckillAdmin> list(SeckillAdmin record);

}