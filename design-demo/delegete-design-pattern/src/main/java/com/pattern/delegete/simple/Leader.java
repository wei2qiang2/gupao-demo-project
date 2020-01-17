package com.pattern.delegete.simple;

import java.util.HashMap;
import java.util.Map;

/**
 * @author weiqiang
 * @date 2019/12/24 9:09
 * @decription
 * @updateInformaion
 */
public class Leader {

    private static Map<String, IEmployee> empMap = new HashMap<String, IEmployee>();

    static {
        empMap.put("加密", new EmployeeA());
        empMap.put("业务", new EmployeeB());
        empMap.put("架构", new EmployeeC());
    }

    public void doing(String command) {
        empMap.get(command).ding();
    }
}
