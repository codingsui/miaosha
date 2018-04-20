package com.syl.dao;

import com.syl.entity.Seckill;

import java.util.Date;
import java.util.List;

public interface SeckillDao {
    /**
     * 减库存
     * @param seckillId
     * @param killTIme
     * @return
     */
    int reduceNumber(long seckillId, Date killTIme);


    /**
     * 根据id查询秒杀对象
     * @param seckillId
     * @return
     */
    Seckill queryBiId(long seckillId);

    /**
     * 根据偏移量查询秒杀商品列表
     * @param offet
     * @param limit
     * @return
     */
    List<Seckill> queryAll(int offet,int limit);
}
