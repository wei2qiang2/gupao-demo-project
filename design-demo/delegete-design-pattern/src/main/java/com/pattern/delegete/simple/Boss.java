package com.pattern.delegete.simple;

/**
 * @author weiqiang
 * @date 2019/12/24 9:09
 * @decription
 * @updateInformaion
 */
public class Boss {

    public void doing(String command){
        new Leader().doing(command);
    }
}
