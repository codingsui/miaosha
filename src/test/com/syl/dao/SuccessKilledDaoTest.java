package com.syl.dao;

import com.syl.entity.SuccessKilled;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SuccessKilledDaoTest {
    @Autowired
    private SuccessKilledDao successKilledDao;
    @Test
    public void insertSuccessKilled() throws Exception {
        long id = 1001L;
        long phone = 1311150301;
        int count = successKilledDao.insertSuccessKilled(id,phone);
        System.out.println(count);
    }

    @Test
    public void queryByIdWithSeckill() throws Exception {
        long id = 1001L;
        long phone = 1311150301;
        SuccessKilled count = successKilledDao.queryByIdWithSeckill(id,phone);
        System.out.println(count);
        System.out.println(count.getSeckill());
    }

}