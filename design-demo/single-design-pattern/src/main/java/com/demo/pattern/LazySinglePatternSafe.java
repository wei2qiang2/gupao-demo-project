package com.demo.pattern;

/**
 * 懒汉模式
 * 线程安全
 */
public class LazySinglePatternSafe {
    private static LazySinglePatternSafe lazySinglePatternSafe = null;

    private LazySinglePatternSafe(){}

    public synchronized static LazySinglePatternSafe getInstance(){
        if (lazySinglePatternSafe == null){
            lazySinglePatternSafe = new LazySinglePatternSafe();
        }
        return lazySinglePatternSafe;
    }
}
