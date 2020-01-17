package com.demo.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.List;
import java.util.UUID;

/**
 * @ClassName RdisLock
 * @Description TODO
 * @Author wq
 * @Date 2019/5/8 9:27
 * @Version 1.0.0
 */
public class RdisLock {
    /**
     * @param key   锁名称
     * @param timeout   超时时间
     * @return
     * @throws InterruptedException
     */
    public String getLock(String key, int timeout) throws InterruptedException {
        long end = System.currentTimeMillis() + timeout;

        UUID uuid = UUID.fromString(UUID.randomUUID().toString());
        //从线程池中获取Jedis
        Jedis jedis = RedisManager.getJedis();
        //进行线程阻塞    当应用没拿到锁的时候不立即返回，相当于重试机制，
        //当重试时间大于timeout的时候才会返回获得锁失败
        while (System.currentTimeMillis() < end) {
            //进行原子操作setnx设置值，成功返回1
            Long setnxResult = jedis.setnx(key, String.valueOf(uuid));

            //当设置成功的时候就表示已经拿到了锁，返回value
            if (setnxResult == 1) {
                //设置过期时间
                //当某个应用拿到锁之后避免一直占有锁，不能主动释放，设置过期时间后会自动释放锁
                jedis.expire(key, timeout);
                return uuid.toString();
            }

            //检测过期时间
            if (jedis.ttl(key) == -1) {
                jedis.expire(key, timeout);
            }

            Thread.sleep(1000);
        }
        return null;
    }

    /**
     * 使用lua脚本释放锁
     * @param lockName
     * @param identity
     * @return
     */
    public boolean releaseLockWithLua(String lockName, String identity){
        Jedis jedis = RedisManager.getJedis();
        String luaScript = "if redis.call('get', KEYS[1] == ARGV[1]) then" +
                "return redis.call('del',KEYS[1]) " +
                "else return 0 end";
        Long rs = (Long) jedis.eval(luaScript, 1, new String[]{lockName, identity});
        if (rs.intValue() > 0){
            return true;
        }
        return false;
    }

    /**
     * 释放锁
     * @param key 锁的key
     * @param value 锁的key对应的value值
     * @return
     */
    public boolean releaseLock(String key, String value) {
        //从连接池获取连接
        Jedis jedis = RedisManager.getJedis();
        //阻塞当前线程
        while (true){
            //监控一个或多个key值的变化，一旦这些key的value发生变化那么后边的事务便不会执行
            jedis.watch(key);
            //校验传入的参数value是否是key对应的value值
            if (value.equals(jedis.get(key))){
                //开启事务
                Transaction transaction = jedis.multi();
                //删除key，释放锁
                transaction.del(key);
                //提交事务
                List<Object> list = transaction.exec();
                //事务提交成功
                if (list == null){
                    //释放失败继续释放
                    continue;
                }
                return true;
            }
            jedis.unwatch();
            break;
        }
        return  false;
    }

    public static void main(String[] args) throws InterruptedException {
        RdisLock lock = new RdisLock();
        String value1 = lock.getLock("lock", 10000);
        if (value1 != null){
            System.out.println("成功:"+value1);
        }else {
            System.out.println("1失败");
        }

        lock.releaseLock("lock", value1);

        String value2 = lock.getLock("lock", 10000);
        if (value2 != null){
            System.out.println("成功:"+value2);
        }else{
            System.out.println("2失败");
        }
    }

}
