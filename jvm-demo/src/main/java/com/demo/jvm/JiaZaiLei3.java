package com.demo.jvm;

public class JiaZaiLei3 {

   static class Hello{
       /**
        * 多线程同时创建对象时只执行一次
        * Thread-0start
        * Thread-0init ...
        * Thread-1start
        * Thread-1end
        * Thread-0end
        */
       /**
        * 执行这块代码的时候会有个指令码做同步操作
        */
       static {
           System.out.println(Thread.currentThread().getName() + "init ...");
           try {
               Thread.sleep(3000);
           } catch (InterruptedException e) {
               e.printStackTrace();
           }
       }
   }

    public static void main(String[] args) {
        new Thread(new Runnable() {
            public void run() {
                System.out.println(Thread.currentThread().getName() + "start");
                Hello jiaZaiLei3 = new Hello();
                System.out.println(Thread.currentThread().getName() + "end");
            }
        }).start();

        new Thread(new Runnable() {
            public void run() {
                System.out.println(Thread.currentThread().getName() + "start");
                Hello jiaZaiLei3 = new Hello();
                System.out.println(Thread.currentThread().getName() + "end");
            }
        }).start();
    }
}
