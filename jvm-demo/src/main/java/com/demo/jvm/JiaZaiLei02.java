package com.demo.jvm;

public class JiaZaiLei02 {

    public static int i = 1;

    static  class Sub extends JiaZaiLei02{
        public static int B = i;
    }

    static {
        i = 2;
    }

    public static void main(String[] args) {
        System.out.println(Sub.B);//2
    }




    /**
     *"C:\Program Files\Java\jdk1.8.0_101\bin\javap.exe" -verbose JiaZaiLei02.class
     * Classfile /E:/IDEA/demo/gupao-demo-project/jvm-demo/src/main/java/com/demo/jvm/JiaZaiLei02.class
     *   Last modified 2019-10-11; size 584 bytes
     *   MD5 checksum de3650ad01a407762b64d07eaa48c130
     *   Compiled from "JiaZaiLei02.java"
     * public class com.demo.jvm.JiaZaiLei02
     *   minor version: 0
     *   major version: 52
     *   flags: ACC_PUBLIC, ACC_SUPER
     * Constant pool:
     *    #1 = Methodref          #7.#22         // java/lang/Object."<init>":()V
     *    #2 = Fieldref           #23.#24        // java/lang/System.out:Ljava/io/PrintStream;
     *    #3 = Fieldref           #8.#25         // com/demo/jvm/JiaZaiLei02$Sub.B:I
     *    #4 = Methodref          #26.#27        // java/io/PrintStream.println:(I)V
     *    #5 = Fieldref           #6.#28         // com/demo/jvm/JiaZaiLei02.i:I
     *    #6 = Class              #29            // com/demo/jvm/JiaZaiLei02
     *    #7 = Class              #30            // java/lang/Object
     *    #8 = Class              #31            // com/demo/jvm/JiaZaiLei02$Sub
     *    #9 = Utf8               Sub
     *   #10 = Utf8               InnerClasses
     *   #11 = Utf8               i
     *   #12 = Utf8               I
     *   #13 = Utf8               <init>
     *   #14 = Utf8               ()V
     *   #15 = Utf8               Code
     *   #16 = Utf8               LineNumberTable
     *   #17 = Utf8               main
     *   #18 = Utf8               ([Ljava/lang/String;)V
     *   #19 = Utf8               <clinit>
     *   #20 = Utf8               SourceFile
     *   #21 = Utf8               JiaZaiLei02.java
     *   #22 = NameAndType        #13:#14        // "<init>":()V
     *   #23 = Class              #32            // java/lang/System
     *   #24 = NameAndType        #33:#34        // out:Ljava/io/PrintStream;
     *   #25 = NameAndType        #35:#12        // B:I
     *   #26 = Class              #36            // java/io/PrintStream
     *   #27 = NameAndType        #37:#38        // println:(I)V
     *   #28 = NameAndType        #11:#12        // i:I
     *   #29 = Utf8               com/demo/jvm/JiaZaiLei02
     *   #30 = Utf8               java/lang/Object
     *   #31 = Utf8               com/demo/jvm/JiaZaiLei02$Sub
     *   #32 = Utf8               java/lang/System
     *   #33 = Utf8               out
     *   #34 = Utf8               Ljava/io/PrintStream;
     *   #35 = Utf8               B
     *   #36 = Utf8               java/io/PrintStream
     *   #37 = Utf8               println
     *   #38 = Utf8               (I)V
     * {
     *   public static int i;
     *     descriptor: I
     *     flags: ACC_PUBLIC, ACC_STATIC
     *
     *   public com.demo.jvm.JiaZaiLei02();
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
     *          3: getstatic     #3                  // Field com/demo/jvm/JiaZaiLei02$Sub.B:I
     *          6: invokevirtual #4                  // Method java/io/PrintStream.println:(I)V
     *          9: return
     *       LineNumberTable:
     *         line 11: 0
     *         line 12: 9
     *
     *   static {};
     *     descriptor: ()V
     *     flags: ACC_STATIC
     *     Code:
     *       stack=1, locals=0, args_size=0
     *          0: iconst_1
     *          1: putstatic     #5                  // Field i:I
     *          4: iconst_2
     *          5: putstatic     #5                  // Field i:I
     *          8: return
     *       LineNumberTable:
     *         line 5: 0
     *         line 7: 4
     *         line 8: 8
     * }
     * SourceFile: "JiaZaiLei02.java"
     * InnerClasses:
     *      static #9= #8 of #6; //Sub=class com/demo/jvm/JiaZaiLei02$Sub of class com/demo/jvm/JiaZaiLei02
     *
     * Process finished with exit code 0
     */
}
