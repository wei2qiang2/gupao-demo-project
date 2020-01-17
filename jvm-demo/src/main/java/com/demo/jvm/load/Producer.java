package com.demo.jvm.load;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Producer implements Runnable {

    private volatile boolean isRunning = true;
    private BlockingQueue queue;
    private static AtomicInteger count = new AtomicInteger();
    private static final int DEFAULT_RANGE_FOR_SLEEP = 1000;

    public Producer(BlockingQueue<String> queue) {
        this.queue = queue;
    }
    public void stop(){
        this.isRunning = false;
    }

    @Override
    public void run() {

        String data = null;
        Random r = new Random();
        System.out.println("producer is start...");
        try{
            while (isRunning){
                System.out.println("正在生产数据......");
                data = "data" + new byte[1024 * 1024] + count.incrementAndGet();
                System.out.println("将数据：" + data +"放入队列");
                if (!queue.offer(data, 2, TimeUnit.SECONDS)){
                    System.out.println("放入数据失败： " + data);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }finally {
            System.out.println("退出生产者线程");
        }

    }
}
