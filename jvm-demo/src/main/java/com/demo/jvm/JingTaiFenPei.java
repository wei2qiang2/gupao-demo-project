package com.demo.jvm;

/**
 * 静态分配
 * 在编译期的时候就能够确定类型（用javap反翻译的字节码和运行的结果就可以看出来）
 */
public class JingTaiFenPei {

    static class Super {}
    static class Sub1 extends Super {}
    static class Sub2 extends Super {}

    public void test(Super parent){
        System.out.println("parent");
    }

    public void test(Sub1 sub1){
        System.out.println("sub1");
    }

    public void test(Sub2 sub2){
        System.out.println("sub2");
    }

    public static void main(String[] args) {
        JingTaiFenPei parent = new JingTaiFenPei();
        Super sub1 = new Sub1();
        Super sub2 = new Sub2();

        parent.test(sub1);//parent
        parent.test(sub2);//parent

        parent.test((Sub1) sub1);//sub1
    }

}
