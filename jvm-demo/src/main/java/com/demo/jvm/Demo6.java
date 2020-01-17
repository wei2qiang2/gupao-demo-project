package com.demo.jvm;

import java.util.ArrayList;
import java.util.List;

public class Demo6 {

    /**
     * -verbose:gc
     * -XX:+PrintGCDetails
     * -Xms10M
     * -Xms10M
     * -Xmn5M
     * -XX:+HeapDumpOnOutOfMemoryError
     * -XX:HeapDumpPath=/demo6.dump
     */

    private static List<byte[]> list = new ArrayList<byte[]>();

    public static void main(String[] args) {
        byte [] b1 = new byte[2 * 1024 *1024];
        byte [] b2 = new byte[2 * 1024 *1024];
        byte [] b3 = new byte[2 * 1024 *1024];
        byte [] b4 = new byte[4 * 1024 *1024];

        list.add(b1);
        list.add(b2);
        list.add(b3);
        list.add(b4);
    }
}
