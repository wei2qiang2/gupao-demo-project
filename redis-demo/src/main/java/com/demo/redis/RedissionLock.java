package com.demo.redis;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * @ClassName RedissionLock
 * @Description TODO
 * @Author wq
 * @Date 2019/6/10 15:50
 * @Version 1.0.0
 */
public class RedissionLock {

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            new Thread(() ->{
                getLock();
            }).start();
        }
    }

    public static void getLock(){
        RedissonClient redissionClient = RedisManager.getRedissionClient();
        RLock updateOrderLock = redissionClient.getLock("updateOrder");
        try {
            //最多获取锁的时间为100s, 超时释放锁的时间为10s
            boolean tryLock = updateOrderLock.tryLock(0, 100, TimeUnit.SECONDS);
            System.out.println(tryLock);
            if (tryLock){
                System.out.println(Thread.currentThread().getId()+"获取锁成功!");
            }else {
                System.out.println("获取锁失败!");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            updateOrderLock.unlock();
            redissionClient.shutdown();
        }
    }
}
