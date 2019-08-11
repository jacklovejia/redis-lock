package com.jack.redislock;

import com.jack.redislock.service.GoodService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisLockApplicationTests {


    @Before
    public void before() {
        System.out.println("开始测试");
    }

    @After
    public void after() {
        System.out.println("测试结束=========================");
    }

    @Autowired
    private GoodService goodService;

    @Test
    public void contextLoads() {
        int id = 1;
        final int threadNum = 2;
        CountDownLatch c = new CountDownLatch(threadNum);
        CountDownLatch c2 = new CountDownLatch(1);
//        Thread[] threads = new Thread[threadNum];
        System.out.println("创建线程");
        for (int i = 0; i < 200; i++) {
            Thread thread = new Thread(() -> {
                try {
                    c.await();
                    goodService.miaosha(id);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });

//            threads[i] = thread;
            thread.start();
            c.countDown();
        }
    }

}
