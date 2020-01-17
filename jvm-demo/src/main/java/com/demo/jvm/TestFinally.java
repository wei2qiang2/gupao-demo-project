package com.demo.jvm;

public class TestFinally {

    public static void main(String[] args) {
        System.out.println(test());
    }

    /**
     *          0: ldc           #5                  // String 123
     *          2: astore_0
     *          3: aload_0  加载0位置的数据
     *          4: astore_1    存在1的位置
     *          5: ldc           #6                  // String 789
     *          7: astore_0     存在0的位置
     *          8: aload_1      加载1位置的数据
     *          9: areturn      返回
     *         10: astore_2
     *         11: ldc           #6                  // String 789
     *         13: astore_0
     *         14: aload_2
     *         15: athrow
     * @return
     */
    public static String test(){
        String str = "123";
        try{
            return str;
        }finally {
            str = "789";
        }
    }
}

