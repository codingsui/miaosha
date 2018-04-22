package com.syl.service;

import com.syl.dto.Exposer;
import com.syl.dto.SeckillExecution;
import com.syl.entity.Seckill;
import com.syl.exception.RepeatKillException;
import com.syl.exception.SeckillException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-*.xml"})
public class SeckillServiceTest {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SeckillService seckillService;
    @Test
    public void getSeckillList() throws Exception {
        List<Seckill> list = seckillService.getSeckillList();
        logger.info("list={}",list);
    }

    @Test
    public void getById() throws Exception {
        long id = 1000;
        Seckill seckill = seckillService.getById(id);
        logger.info("seckill={}",seckill);
    }

    @Test
    public void exportSeckillUrl() throws Exception {
        long id = 1000;
        Exposer exposer = seckillService.exportSeckillUrl(id);
        logger.info("exposer={}",exposer);
    }

    @Test
    public void executeSeckill() throws Exception {
        long id = 1000;
        String md5 ="23531ffa193049bd1d6ed0973c71dd65";
        long phone = 18738997071L;
        try {
            SeckillExecution exposer = seckillService.executeSeckill(id,phone,md5);
            logger.info("exposer={}",exposer);
        }catch (RepeatKillException e){
            logger.error(e.getMessage());
        }catch (SeckillException e){
            logger.error(e.getMessage());
        }
    }

    @Test
    public void executeSeckillProdure() throws Exception {
        long seckillId = 1001;
        long phone = 18738997075L;
        Exposer exposer = seckillService.exportSeckillUrl(seckillId);
        if(exposer.isExposed()){
            String md5 = exposer.getMd5();
            SeckillExecution seckillExecution = seckillService.executeSeckillProcedure(seckillId,phone,md5);
            logger.info("seckillExecution:{}",seckillExecution);
        }
    }
}