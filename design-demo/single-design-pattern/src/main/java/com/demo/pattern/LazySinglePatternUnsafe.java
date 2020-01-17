package com.demo.pattern;

/**
 * 懒汉模式
 * 非线程安全
 */
public class LazySinglePatternUnsafe {

    private static LazySinglePatternUnsafe singlePatternUnsafe = null;

    private LazySinglePatternUnsafe(){}

    public static LazySinglePatternUnsafe getSinglePatternUnsafe(){
        if (singlePatternUnsafe == null){
            singlePatternUnsafe = new LazySinglePatternUnsafe();
        }
        return singlePatternUnsafe;
    }
}
