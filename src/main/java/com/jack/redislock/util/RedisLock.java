package com.jack.redislock.util;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.types.Expiration;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RedisLock implements Lock {

    private StringRedisTemplate stringRedisTemplate; // redis连接
    private String sourceName; // 锁资源的名称, 可以是某个商品ID
    private String top; // 发布订阅的主题
    private long timeOut; // 设置超时时间(秒)

    @Override
    public String toString() {
        return "RedisLock{" +
                "stringRedisTemplate=" + stringRedisTemplate +
                ", sourceName='" + sourceName + '\'' +
                ", top='" + top + '\'' +
                ", timeOut=" + timeOut +
                '}';
    }

    public RedisLock(StringRedisTemplate stringRedisTemplate, String sourceName, String top, long timeOut) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.sourceName = sourceName;
        this.top = top;
        this.timeOut = timeOut;
    }

    Lock lock = new ReentrantLock();
    @Override
    public void lock() {
        lock.lock();
        try {
            while (!tryLock()) {
                stringRedisTemplate.execute(new RedisCallback<Long>() {
                    long a = 0l;
                    // 订阅top 锁的消息
                    @Override
                    public Long doInRedis(RedisConnection redisConnection) throws DataAccessException {
                        // subscribe 方法会立马返回结果, 返回是否订阅成功, 不满足实际需求 所以接下来处理
                        try {
                            CountDownLatch coun = new CountDownLatch(1);
                            redisConnection.subscribe((message, bytes) -> {
                                // 收到通知后执行的
                                a = System.currentTimeMillis();
                                coun.countDown();
                            }, top.getBytes());
                            coun.await(timeOut, TimeUnit.SECONDS);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return 0L;
                    }
                });
            }
        } finally {
            lock.unlock();
        }

    }


    @Override
    public boolean tryLock() {
        Boolean aBoolean = stringRedisTemplate.execute((RedisCallback<Boolean>) redisConnection -> {
            System.out.println(Thread.currentThread().getName()+"进入tryLock"+redisConnection);
            String value = "lock";
            Boolean set = redisConnection.set(sourceName.getBytes(), value.getBytes(), Expiration.seconds(timeOut), RedisStringCommands.SetOption.SET_IF_ABSENT);
            System.out.println(Thread.currentThread().getName()+"try获取锁:"+set);
            return set;
        });
        return aBoolean;
    }

    @Override
    public void unlock() {
        stringRedisTemplate.delete(sourceName);
        stringRedisTemplate.execute(new RedisCallback<Long>() {
            // 发布释放锁的消息
            @Override
            public Long doInRedis(RedisConnection redisConnection) throws DataAccessException {
                String value = "unLock";
                System.out.println(Thread.currentThread().getName()+"发布消息消息:"+System.currentTimeMillis());
                Long publish = redisConnection.publish(top.getBytes(), value.getBytes());
                return publish;
            }
        });
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }


    @Override
    public Condition newCondition() {
        return null;
    }
}
