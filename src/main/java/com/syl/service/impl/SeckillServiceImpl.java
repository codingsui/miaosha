package com.syl.service.impl;

import com.syl.dao.SeckillDao;
import com.syl.dao.SuccessKilledDao;
import com.syl.dao.cache.RedisDao;
import com.syl.dto.Exposer;
import com.syl.dto.SeckillExecution;
import com.syl.entity.Seckill;
import com.syl.entity.SuccessKilled;
import com.syl.enums.SeckillStateEnum;
import com.syl.exception.RepeatKillException;
import com.syl.exception.SeckillCloseException;
import com.syl.exception.SeckillException;
import com.syl.service.SeckillService;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("seckillSerice")
public class SeckillServiceImpl implements SeckillService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SeckillDao seckillDao;
    @Autowired
    private SuccessKilledDao successKilledDao;
    @Autowired
    private RedisDao redisDao;

    private final String slat = "jdksun##00*";

    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0,4);
    }

    public Seckill getById(long seckillId) {
        return seckillDao.queryBiId(seckillId);
    }

    @Transactional
    /**
     * 使用注解控制事务方法的优点
     * 1.开发团队达成一致约定，明确标注事务方法的编程风格
     * 2.保证事务方法的执行时间尽可能短，不要穿插其他的网络操作如Reids等。如果真的需要的话，剥离到事务方法之外
     * 3.不是所有的方法都需要事务，如只有一条修改操作，只读操作不需要事务控制
     */
    public Exposer exportSeckillUrl(long seckillId) {
        //缓存优化
        /**
         * get from cache
         * if null
         * get db
         * else
         *  put cache
         */
        //1.访问redis
        Seckill seckill = redisDao.getSeckill(seckillId);
        if (seckill == null){
            seckill = this.getById(seckillId);
            if (seckill == null){
                return new Exposer(false,seckillId);
            }else{
                redisDao.putSeckill(seckill);
            }
        }

        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();

        Date nowTime = new Date();
        if (nowTime.getTime()<startTime.getTime()||nowTime.getTime()>endTime.getTime()){
            return new Exposer(false,seckillId,nowTime.getTime(),startTime.getTime(),endTime.getTime());
        }
        //TODO
        String md5 = getMD5(seckillId);

        return new Exposer(true,md5,seckillId);
    }

    private String getMD5(long seckillId){
        String base = seckillId + "/" + slat;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws SeckillException, RepeatKillException, SeckillCloseException {
        if (md5 == null || !md5.equals(getMD5(seckillId))){
            throw new SeckillException("seckill data rewrite");
        }
        //执行秒杀逻辑
        //记录购买行为 减库存

        try {
            //记录购买行为
            int insertCount = successKilledDao.insertSuccessKilled(seckillId,userPhone);
            if (insertCount<=0){
                throw new RepeatKillException("seckill repeated");
            }else {
                //减库存
                Date nowTime = new Date();
                int updateCount = seckillDao.reduceNumber(seckillId,nowTime);
                if (updateCount <=0 ){
                    throw new SeckillCloseException("seckill closed");
                }else {
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId,userPhone);
                    return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS,successKilled);
                }
            }

        }catch (SeckillCloseException e){
            throw e;
        }catch (RepeatKillException e){
            throw e;
        } catch (Exception e){
            logger.error(e.getMessage(),e);
            //编译器异常转换为运行期异常
            throw new SeckillException("seckill inner error:"+e.getMessage());
        }


    }

    public SeckillExecution executeSeckillProcedure(long seckillId, long userPhone, String md5)  {
        if(md5 == null ||!md5.equals(getMD5(seckillId))){
            return new SeckillExecution(seckillId,SeckillStateEnum.DATA_REWRITE);
        }
        Date killTime = new Date();
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("seckillId",seckillId);
        map.put("phone",userPhone);
        map.put("killTime",killTime);
        map.put("result",null);
        try {
            seckillDao.killByProcedure(map);
            //获取result;
            int result = MapUtils.getInteger(map,"result",-2);
            if (result == 1){
                SuccessKilled sk = successKilledDao.queryByIdWithSeckill(seckillId,userPhone);
                return new SeckillExecution(seckillId,SeckillStateEnum.SUCCESS,sk);
            }else{
                return new SeckillExecution(seckillId,SeckillStateEnum.stateOf(result));
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return new SeckillExecution(seckillId,SeckillStateEnum.INNER_ERROR);
        }
    }
}
