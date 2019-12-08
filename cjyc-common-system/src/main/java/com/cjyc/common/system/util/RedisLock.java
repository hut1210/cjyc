package com.cjyc.common.system.util;

import com.cjkj.common.redis.lock.RedisDistributedLock;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Component
@Slf4j
@Scope("singleton")
public class RedisLock extends RedisDistributedLock {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    private ThreadLocal<String> lockFlag = new ThreadLocal<>();
    private static final String UNLOCK_LUA = "if redis.call(\"get\",KEYS[1]) == ARGV[1] then     return redis.call(\"del\",KEYS[1]) else     return 0 end ";


    public RedisLock(RedisTemplate<String, Object> redisTemplate) {
        super(redisTemplate);
    }

    public boolean lock(String key, Object value, long expire, int retryTimes, long sleepMillis) {
        boolean result;
        for(result = this.setRedis(key, value, expire); !result && retryTimes-- > 0; result = this.setRedis(key, value, expire)) {
            try {
                log.debug("get redisDistributeLock failed, retrying..." + retryTimes);
                Thread.sleep(sleepMillis);
            } catch (InterruptedException var9) {
                log.warn("Interrupted!", var9);
                Thread.currentThread().interrupt();
            }
        }

        return result;
    }

    private boolean setRedis(String key, Object value, long expire) {
        try {
            String status = this.redisTemplate.execute((RedisCallback<String>) (connection) -> {
                Object nativeConnection = connection.getNativeConnection();
                String v;
                if(value == null){
                    v = UUID.randomUUID().toString();
                }else{
                    v = String.valueOf(value);
                }
                this.lockFlag.set(v);
                String result = null;
                if (nativeConnection instanceof JedisCluster) {
                    result = ((JedisCluster)nativeConnection).set(key, v, "NX", "PX", expire);
                } else if (nativeConnection instanceof Jedis) {
                    result = ((Jedis)nativeConnection).set(key, v, "NX", "PX", expire);
                }

                return result;
            });
            return !StringUtils.isEmpty(status);
        } catch (Exception var5) {
            log.error("set redisDistributeLock occured an exception", var5);
            return false;
        }
    }

    public boolean releaseLock(Collection<String> keys) {
        try {
            List<String> keyList = Lists.newArrayList(keys);
            List<String> args = Lists.newArrayList();
            args.add(this.lockFlag.get());
            Long result = this.redisTemplate.execute((RedisCallback<Long>) (connection) -> {
                Object nativeConnection = connection.getNativeConnection();
                if (nativeConnection instanceof JedisCluster) {
                    return (Long)((JedisCluster)nativeConnection).eval(UNLOCK_LUA, keyList, args);
                } else {
                    return nativeConnection instanceof Jedis ? (Long)((Jedis)nativeConnection).eval(UNLOCK_LUA, keyList, args) : 0L;
                }
            });
            boolean var5 = result != null && result > 0L;
            return var5;
        } catch (Exception var9) {
            log.error("release redisDistributeLock occured an exception", var9);
        } finally {
            this.lockFlag.remove();
        }

        return false;
    }



}
