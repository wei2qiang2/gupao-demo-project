package com.demo.jvm;

public class JiaZaiLei {

    static {
        //只是在变量表找到了i，可以赋值了；在变量表做的事
        i = 0;

        //需要找到内存中的地址； 往前找，没有定义，读不到这个便可
        //        System.out.println(i);

    }
    //在内存中分配地址并赋值
    static int i = 0;

    public static void main(String[] args) {
        System.out.println(i);
    }
    /**
     * "C:\Program Files\Java\jdk1.8.0_101\bin\javap.exe" -verbose JiaZaiLei.class
     * Classfile /E:/IDEA/demo/gupao-demo-project/jvm-demo/src/main/java/com/demo/jvm/JiaZaiLei.class
     *   Last modified 2019-10-11; size 491 bytes
     *   MD5 checksum e48df735651947b94a24054ed1f586ae
     *   Compiled from "JiaZaiLei.java"
     * public class com.demo.jvm.JiaZaiLei
     *   minor version: 0
     *   major version: 52
     *   flags: ACC_PUBLIC, ACC_SUPER
     * Constant pool:
     *    #1 = Methodref          #6.#18         // java/lang/Object."<init>":()V
     *    #2 = Fieldref           #19.#20        // java/lang/System.out:Ljava/io/PrintStream;
     *    #3 = Fieldref           #5.#21         // com/demo/jvm/JiaZaiLei.i:I
     *    #4 = Methodref          #22.#23        // java/io/PrintStream.println:(I)V
     *    #5 = Class              #24            // com/demo/jvm/JiaZaiLei
     *    #6 = Class              #25            // java/lang/Object
     *    #7 = Utf8               i
     *    #8 = Utf8               I
     *    #9 = Utf8               <init>
     *   #10 = Utf8               ()V
     *   #11 = Utf8               Code
     *   #12 = Utf8               LineNumberTable
     *   #13 = Utf8               main
     *   #14 = Utf8               ([Ljava/lang/String;)V
     *   #15 = Utf8               <clinit>
     *   #16 = Utf8               SourceFile
     *   #17 = Utf8               JiaZaiLei.java
     *   #18 = NameAndType        #9:#10         // "<init>":()V
     *   #19 = Class              #26            // java/lang/System
     *   #20 = NameAndType        #27:#28        // out:Ljava/io/PrintStream;
     *   #21 = NameAndType        #7:#8          // i:I
     *   #22 = Class              #29            // java/io/PrintStream
     *   #23 = NameAndType        #30:#31        // println:(I)V
     *   #24 = Utf8               com/demo/jvm/JiaZaiLei
     *   #25 = Utf8               java/lang/Object
     *   #26 = Utf8               java/lang/System
     *   #27 = Utf8               out
     *   #28 = Utf8               Ljava/io/PrintStream;
     *   #29 = Utf8               java/io/PrintStream
     *   #30 = Utf8               println
     *   #31 = Utf8               (I)V
     * {
     *   static int i;
     *     descriptor: I
     *     flags: ACC_STATIC
     *
     *   public com.demo.jvm.JiaZaiLei();
     *     descriptor: ()V
     *     flags: ACC_PUBLIC
     *     Code:
     *       stack=1, locals=1, args_size=1
     *          0: aload_0
     *          1: invokespecial #1                  // Method java/lang/Object."<init>":()V
     *          4: return
     *       LineNumberTable:
     *         line 3: 0
     *
     *   public static void main(java.lang.String[]);
     *     descriptor: ([Ljava/lang/String;)V
     *     flags: ACC_PUBLIC, ACC_STATIC
     *     Code:
     *       stack=2, locals=1, args_size=1
     *          0: getstatic     #2                  // Field java/lang/System.out:Ljava/io/PrintStream;
     *          3: getstatic     #3                  // Field i:I
     *          6: invokevirtual #4                  // Method java/io/PrintStream.println:(I)V
     *          9: return
     *       LineNumberTable:
     *         line 17: 0
     *         line 18: 9
     *
     *   static {};
     *     descriptor: ()V
     *     flags: ACC_STATIC
     *     Code:
     *       stack=1, locals=0, args_size=0
     *          0: iconst_0
     *          1: putstatic     #3                  // Field i:I
     *          4: iconst_0
     *          5: putstatic     #3                  // Field i:I
     *          8: return
     *       LineNumberTable:
     *         line 7: 0
     *         line 14: 4
     * }
     * SourceFile: "JiaZaiLei.java"
     *
     * Process finished with exit code 0
     */
}
