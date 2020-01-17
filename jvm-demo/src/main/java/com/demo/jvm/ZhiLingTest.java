package com.demo.jvm;

public class ZhiLingTest {

    /**
     *          0: bipush        8
     *          2: istore_1
     *          3: bipush        10
     *          5: istore_2
     *          6: iinc          1, 1
     *          9: iload_1
     *         10: iload_2
     *         11: if_icmple     25                  // if比较指令
     *         14: getstatic     #2                  // Field java/lang/System.out:Ljava/io/PrintStream;
     *         17: ldc           #3                  // String  i > count
     *         19: invokevirtual #4                  // Method java/io/PrintStream.println:(Ljava/lang/String;)V
     *         22: goto          33
     *         25: getstatic     #2                  // Field java/lang/System.out:Ljava/io/PrintStream;
     *         28: ldc           #5                  // String i <= count
     *         30: invokevirtual #4                  // Method java/io/PrintStream.println:(Ljava/lang/String;)V
     *         33: return
     * @param args
     */
    public static void main(String[] args) {
        int i = 8, count = 10;
        if (++i > count){
            System.out.println(" i > count");
        }else{
            System.out.println("i <= count");
        }
    }
}
