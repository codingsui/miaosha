package com.syl.dao;


import com.syl.entity.Seckill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})

public class SeckillDaoTest {
    @Autowired
    private SeckillDao seckillDao;
    @Test
    public void reduceNumber() throws Exception {
        Date killTime = new Date();
        int count = seckillDao.reduceNumber(1000L,killTime);
        System.out.println(count);
    }

    @Test
    public void queryBiId() throws Exception {
        long id = 1000;
        Seckill seckill = seckillDao.queryBiId(id);
        System.out.println(seckill.getName());
        System.out.println(seckill);

    }

    @Test
    public void queryAll() throws Exception {
        List<Seckill> list = seckillDao.queryAll(0,100);
        for(Seckill item : list){
            System.out.println(item);
        }
    }



}
