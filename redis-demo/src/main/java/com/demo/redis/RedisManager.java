package com.demo.redis;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @ClassName RedisManager
 * @Description TODO
 * @Author wq
 * @Date 2019/5/8 9:16
 * @Version 1.0.0
 */
public class RedisManager {

    private static JedisPool jedisPool = null;

    static {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(20);
        jedisPoolConfig.setMaxIdle(10);

        jedisPool = new JedisPool(jedisPoolConfig, "119.23.220.195", 6379);
    }

    public static Jedis getJedis() {
        if (jedisPool != null) {
            return jedisPool.getResource();
        }
        throw new RuntimeException("jedis pool is null");
    }

    public static void main(String[] args) {
        getRedissionClient();
        Jedis jedis = getJedis();
        jedis.set("lock", "lock");
        String a = jedis.get("lock");
        System.out.println(a);
    }

    /**
     * 获取RedissionClient
     * @return
     */
    public static RedissonClient getRedissionClient(){
        Config config = new Config();
        config.useSingleServer().setAddress("redis://119.23.220.195:6379");
        RedissonClient redissonClient = Redisson.create(config);
        System.out.println(redissonClient);
        return redissonClient;
    }
}
