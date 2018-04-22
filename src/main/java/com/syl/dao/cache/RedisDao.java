package com.syl.dao.cache;

import com.syl.entity.Seckill;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisDao {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final JedisPool jedisPool;
    private Schema<Seckill> schema = RuntimeSchema.getSchema(Seckill.class);
    public RedisDao(String ip,int port){
        jedisPool = new JedisPool(ip,port);
    }

    public Seckill getSeckill(long seckillId) {
        try {
            Jedis jedis = jedisPool.getResource();
            try {
                jedis.auth("12345678");
                String key = "seckill:"+seckillId;
                byte[] bytes = jedis.get(key.getBytes());
                if (bytes != null){
                    Seckill seckill = schema.newMessage();
                    ProtostuffIOUtil.mergeFrom(bytes,seckill,schema);
                    //seckill 被反序列化
                    return seckill;
                }
            }finally {
                jedis.close();
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return null;
    }

    public String putSeckill(Seckill seckill) {
        // set object -> byte[] -> 序列化
        try {
            Jedis jedis = jedisPool.getResource();
            try {
                jedis.auth("12345678");
                String key = "seckill:"+seckill.getSeckillId();
                byte[] bytes = ProtostuffIOUtil.toByteArray(seckill,schema, LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
                int timeout = 60*60;
                String result = jedis.setex(key.getBytes(),timeout,bytes);
                return result;
            }finally {
                jedis.close();
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return null;
    }
}
