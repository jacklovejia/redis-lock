package com.jack.redislock;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;
@RunWith(SpringRunner.class)
@SpringBootTest
public class Test {
    //并发数
    private static final int threadNum = 2000;

    //倒计时数 发令枪 用于制造线程的并发执行
    private static CountDownLatch cdl = new CountDownLatch(threadNum);

@org.junit.Test
    public void test() {
        System.out.println("1");
        for(int i = 0;i< threadNum;i++) {
            //new多个子线程
            new Thread(new ThreadClass()).start();
            //计数器-1
            cdl.countDown();
        }
        try {
            //主线程 等待 子线程执行完 等待
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //多线程执行类
    public class ThreadClass implements Runnable{

        @Override
        public void run() {
            try {
                //所有子线程在这里等待，当所有线程实例化后，停止等待
                cdl.await();
                System.out.println("==========");
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            //执行业务方法

        }

    }
}
