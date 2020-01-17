package com.pattern.delegete.simple;

/**
 * @author weiqiang
 * @date 2019/12/24 9:16
 * @decription
 * @updateInformaion
 */
public class SimpleTest {

    public static void main(String[] args) {
        new Boss().doing("加密");
        new Boss().doing("业务");
        new Boss().doing("架构");
    }
}
