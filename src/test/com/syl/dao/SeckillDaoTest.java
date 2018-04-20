package com.syl.dao;


import com.syl.entity.Seckill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})

public class SeckillDaoTest {
    @Autowired
    private SeckillDao seckillDao;
    @Test
    public void reduceNumber() throws Exception {
        long id = 1000;
        Seckill seckill = seckillDao.queryBiId(id);
        System.out.println(seckill.getName());
        System.out.println(seckill);
    }

    @Test
    public void queryBiId() throws Exception {
    }

    @Test
    public void queryAll() throws Exception {
    }



}
