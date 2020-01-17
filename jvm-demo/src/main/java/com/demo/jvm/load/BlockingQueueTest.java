package com.demo.jvm.load;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * LinkedBlockingQueue :无界阻塞队列
 * ArrayBlockingQueue：有界阻塞队列
 */
public class BlockingQueueTest {

    public static void main(String[] args) {

        BlockingQueue<String> queue = new LinkedBlockingDeque<String>();

        ExecutorService service = Executors.newCachedThreadPool();

        //500个线程发消息
        for (int i = 0; i < 500; i++) {
            Producer producer = new Producer(queue);
            service.execute(producer);
        }

        //5个线程去取
        for (int i = 0; i < 5 ; i++) {
            Consumer consumer = new Consumer(queue);
            service.execute(consumer);
        }
    }
}
