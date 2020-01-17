package com.demo.jvm;

/**
 * slot接口服用
 */
public class SlotFuYong {

    public static void main(String[] args) {
        {
            byte [] buf = new byte[1024 * 1024 * 30];
        }
        System.gc();
    }
}
