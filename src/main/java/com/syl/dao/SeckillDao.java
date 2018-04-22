package com.syl.dao;

import com.syl.entity.Seckill;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface SeckillDao {
    /**
     * 减库存
     * @param seckillId
     * @param killTime
     * @return
     */
    int reduceNumber(@Param("seckillId") long seckillId,@Param("killTime") Date killTime);


    /**
     * 根据id查询秒杀对象
     * @param seckillId
     * @return
     */
    Seckill queryBiId(long seckillId);

    /**
     * 根据偏移量查询秒杀商品列表
     * @param offset
     * @param limit
     * @return
     */
    List<Seckill> queryAll(@Param("offset") int offset, @Param("limit") int limit);

    /**
     * 存储过程秒杀
     * @param paramMap
     */
    void killByProcedure(Map<String,Object> paramMap);
}
