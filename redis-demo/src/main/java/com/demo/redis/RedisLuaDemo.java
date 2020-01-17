package com.demo.redis;

import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;

/**
 * 在一定的时间范围内的操作接口次数设置
 * @ClassName RedisLuaDemo
 * @Description TODO
 * @Author wq
 * @Date 2019/5/8 16:13
 * @Version 1.0.0
 */
public class RedisLuaDemo {
    public static void main(String[] args) {
        Jedis jedis = RedisManager.getJedis();
        String lua = "local num = redis.call('incr',KEYS[1])\n" +
                "if tonumber(num) == 1 then\n" +
                "    redis.call('expire', KEYS[1], ARGV[1])\n" +
                "    return 1\n" +
                "elseif tonumber(num) > tonumber(ARGV[2]) then\n" +
                "    return 0\n" +
                "else \n" +
                "    return 1\n" +
                "end";


        List<String> list = new ArrayList<>();
        list.add("ip-limit:wq1");
        List<String> argvs = new ArrayList<>();

        argvs.add("6000");
        argvs.add("10");
        for (int i = 0; i < 10; i++) {
//            Object eval = jedis.eval(lua,list, argvs);
            //或者执行摘要，不用每次传输脚本
            //jedis.scriptLoad(lua) 摘要映射到本地的lua脚本
            Object evalsha = jedis.evalsha(jedis.scriptLoad(lua), list, argvs);
            System.out.println(evalsha);
        }
    }
}
