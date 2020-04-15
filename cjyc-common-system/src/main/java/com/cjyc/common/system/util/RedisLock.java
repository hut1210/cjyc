package com.cjyc.common.system.util;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.TimeoutUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
@Scope("singleton")
public class RedisLock {
    private final long EXPIRE_MILLIS = 5000L;
    private final int RETRY_TIMES = 30;
    private final long SLEEP_MILLIS = 100L;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    private ThreadLocal<String> lockFlagThreadLocal = new ThreadLocal<>();
    private static final String UNLOCK_LUA = "for i, v in pairs(KEYS) do    " +
                                            "   if redis.call(\"get\", v) ~= ARGV[1] then " +
                                            "      return 0 " +
                                            "   end   " +
                                            "end " +
                                            "for i, v in pairs(KEYS) do " +
                                            "   redis.call(\"del\", v) " +
                                            "end " +
                                            "return #KEYS";

    public RedisLock(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean lock(String key) {
        return this.lock(key, null, EXPIRE_MILLIS, TimeUnit.MILLISECONDS, RETRY_TIMES, SLEEP_MILLIS);
    }

    public boolean lock(String key, int retryTimes) {
        return this.lock(key, null, EXPIRE_MILLIS, TimeUnit.MILLISECONDS, retryTimes, SLEEP_MILLIS);
    }

    public boolean lock(String key, int retryTimes, long sleepMillis) {
        return this.lock(key, null, EXPIRE_MILLIS, TimeUnit.MILLISECONDS, retryTimes, sleepMillis);
    }

    public boolean lock(String key, long expire) {
        return this.lock(key, null, expire, TimeUnit.MILLISECONDS, RETRY_TIMES, SLEEP_MILLIS);
    }

    public boolean lock(String key, long expire, int retryTimes) {
        return this.lock(key, null, expire, TimeUnit.MILLISECONDS, retryTimes, SLEEP_MILLIS);
    }

    public boolean lock(String key, long expire,  int retryTimes, long sleepMillis) {
        return this.lock(key, null, expire, TimeUnit.MILLISECONDS, retryTimes, sleepMillis);
    }

    public boolean lock(String key, long expire, TimeUnit expireTimeUnit, int retryTimes, long sleepMillis) {
        return this.lock(key, null, expire, expireTimeUnit, retryTimes, sleepMillis);
    }


    public boolean lock(String key, Object lockFlag, long expire, int retryTimes, long sleepMillis) {
        return this.lock(key, lockFlag, expire, TimeUnit.MILLISECONDS, retryTimes, sleepMillis);
    }

    public boolean lock(String key, Object lockFlag, long expire, TimeUnit expireTimeUnit, int retryTimes, long sleepMillis) {
        boolean result;
        long expireMillis = TimeoutUtils.toMillis(expire, expireTimeUnit);
        for(result = this.setRedis(key, lockFlag, expireMillis); !result && retryTimes-- > 0; result = this.setRedis(key, lockFlag, expire)) {
            try {
                log.debug("get redisDistributeLock failed, retrying..." + retryTimes);
                Thread.sleep(sleepMillis);
            } catch (InterruptedException e) {
                log.warn("Interrupted!", e);
                Thread.currentThread().interrupt();
            }
        }
        return result;
    }

    private boolean setRedis(String key, Object lockFlag, long expire) {
        try {
            String status = this.redisTemplate.execute((RedisCallback<String>) (connection) -> {
                Object nativeConnection = connection.getNativeConnection();
                String v = getLockFlag(lockFlag);
                lockFlagThreadLocal.set(v);
                String result = null;
                if (nativeConnection instanceof JedisCluster) {
                    result = ((JedisCluster)nativeConnection).set(key, v, "NX", "PX", expire);
                } else if (nativeConnection instanceof Jedis) {
                    result = ((Jedis)nativeConnection).set(key, v, "NX", "PX", expire);
                }

                return result;
            });
            return !StringUtils.isEmpty(status);
        } catch (Exception e) {
            log.error("set redisDistributeLock occured an exception", e);
            return false;
        }
    }

    public boolean releaseLock(String key){
        List<String> list = new ArrayList<>();
        list.add(key);
        return releaseLock(list, null);
    }
    public boolean releaseLock(String key, Object lockFlag){
        List<String> list = new ArrayList<>();
        list.add(key);
        return releaseLock(list, lockFlag);
    }

    public boolean releaseLock(Collection<String> keys){
        return releaseLock(keys, null);
    }

    public boolean releaseLock(Collection<String> keys, Object lockFlag) {
        try {
            List<String> keyList = Lists.newArrayList(keys);
            List<String> args = Lists.newArrayList();
            String v = lockFlag == null || StringUtils.isEmpty(lockFlag) ? lockFlagThreadLocal.get() : String.valueOf(lockFlag);
            args.add(v);
            Long result = this.redisTemplate.execute((RedisCallback<Long>) (connection) -> {
                Object nativeConnection = connection.getNativeConnection();
                if (nativeConnection instanceof JedisCluster) {
                    return (Long)((JedisCluster)nativeConnection).eval(UNLOCK_LUA, keyList, args);
                } else {
                    return nativeConnection instanceof Jedis ? (Long)((Jedis)nativeConnection).eval(UNLOCK_LUA, keyList, args) : 0L;
                }
            });
            boolean flag = result != null && result > 0L;
            log.info("【Redis解锁】：结果{}（{}）", JSON.toJSON(keys), v);
            log.info("【Redis解锁】：结果{}（{}）", flag, flag ? "成功" : "失败");
            return flag;
        } catch (Exception e) {
            log.error("release redisDistributeLock occured an exception", e);
        } finally {
            lockFlagThreadLocal.remove();
        }
        return false;
    }
    public String getLockFlag() {
        return getLockFlag(null);
    }
    public String getLockFlag(Object value) {
        return value == null || StringUtils.isEmpty(String.valueOf(value))? UUID.randomUUID().toString() : String.valueOf(value);
    }

}
