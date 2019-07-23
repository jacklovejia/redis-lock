package com.jack.redislock.service;

import com.jack.redislock.util.RedisLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class GoodService {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    RedisLock redisLock  = null;

    @PostConstruct
    public void init() {
        redisLock = new RedisLock(stringRedisTemplate, "jack", "ID", 10);
    }


    public boolean miaosha(int id) {
        System.out.println(Thread.currentThread().getName()+"====================进入秒杀========================"+redisLock.toString());
        redisLock.lock();
        try {
            boolean result = false;
            // 商品数量减1, redis 减1

            int i = countDown(id);
            if (i == 1) {
                result = true;
                updateCache(id, getCount(id));
            }
            System.out.println(Thread.currentThread().getName()+"秒杀结果:" + result+"时间戳:"+System.currentTimeMillis());
            return result;
        } finally {
            redisLock.unlock();
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public int countDown(int id) {
        String sql = "UPDATE goods SET num = num -1 WHERE id = " + id + " AND num > 0";
        int update = jdbcTemplate.update(sql);
        return update;
    }

    private int getCount(int id) {
        String sql = "SELECT num FROM goods WHERE id = " + id + "";
        return Integer.parseInt(jdbcTemplate.queryForObject(sql, String.class));
    }

    private void updateCache(int id, int num) {
        stringRedisTemplate.opsForValue().set(String.valueOf(id), String.valueOf(num));
    }

}
