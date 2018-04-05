package com.syl.dao;

import com.syl.entity.SuccessKilled;

public interface SuccessKilledDao {
    /**
     * 插入购买明细
     * @param seckillId
     * @param usePhone
     * @return
     */
    int insertSuccessKilled(long seckillId,long usePhone);

    /**
     * 根据id查询SuccessKilled并携带秒杀产品对象
     * @param seckillId
     * @return
     */
    SuccessKilled queryByIdWithSeckill(long seckillId);
}
