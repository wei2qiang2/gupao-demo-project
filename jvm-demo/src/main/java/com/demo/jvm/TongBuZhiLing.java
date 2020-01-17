package com.demo.jvm;

public class TongBuZhiLing {




    /**
     * 下面的不会报错
     *  static int i = 0;
     *  static {
     *         i = 0;
     *         System.out.println(i);
     *     }
     *
     */


    /**
     *          0: aload_0
     *          1: invokespecial #1                  // Method java/lang/Object."<init>":()V
     *          4: return
     */
    //实例方法
    public synchronized void f1(){

    }

    /**
     *          0: aload_0
     *          1: dup
     *          2: astore_1
     *          3: monitorenter 加锁
     *          4: aload_1
     *          5: monitorexit 释放锁
     *          6: goto          14
     *          9: astore_2
     *         10: aload_1
     *         11: monitorexit
     *         12: aload_2
     *         13: athrow
     *         14: return
     */
    public void f2(){
        synchronized (this){

        }
    }

    public static void main(String[] args) {

    }
}
