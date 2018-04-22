package com.syl.service;

import com.syl.dto.Exposer;
import com.syl.dto.SeckillExecution;
import com.syl.entity.Seckill;
import com.syl.exception.RepeatKillException;
import com.syl.exception.SeckillCloseException;
import com.syl.exception.SeckillException;

import java.util.List;

/**
 * 方法定义粒度，参数，返回类型
 */
public interface SeckillService {

    /**
     * 查询所有的记录
     * @return
     */
    List<Seckill>  getSeckillList();

    /**
     * 查询单个记录
     * @param seckillId
     * @return
     */
    Seckill getById(long seckillId);

    /**
     * 秒杀开启，输出秒杀接口的地址
     * 否则输出系统时间和秒杀时间
     * @param seckillId
     */
    Exposer exportSeckillUrl(long seckillId);

    /**
     * 执行秒杀
     * @param seckillId
     * @param userPhone
     * @param md5
     */
    SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
            throws SeckillException,RepeatKillException,SeckillCloseException;

    /**
     * 执行秒杀 by存储过程
     * @param seckillId
     * @param userPhone
     * @param md5
     */
    SeckillExecution executeSeckillProcedure(long seckillId, long userPhone, String md5)
            ;
}
