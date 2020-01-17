package com.demo.jvm;

/**
 * 动态连接
 * 动态绑定
 * 在编译的时候不能够决定类型
 */
public class DongTaiLianJie {

    static class Super{
        public void test(){
            System.out.println("super");
        }
    }

    static class Sub1 extends Super{
        @Override
        public void test() {
            System.out.println("sub1");
        }
    }

    static class Sub2 extends Super{
        @Override
        public void test() {
            System.out.println("sub2");
        }
    }

    public static void main(String[] args) {
        Sub1 c1 = new Sub1();
        Sub2 c2 = new Sub2();
        c1.test();
        c2.test();
    }
}
