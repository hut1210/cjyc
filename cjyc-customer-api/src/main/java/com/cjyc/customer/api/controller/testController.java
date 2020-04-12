package com.cjyc.customer.api.controller;

import com.cjyc.common.system.util.RedisLock;
import com.cjyc.common.system.util.RedisUtils;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@Api(tags = "test")
public class testController {
    @Autowired
    RedisLock redisLock;
    @Autowired
    RedisUtils redisUtils;
    @GetMapping("/test/ds")
    public String test(){
        List<String> list = Lists.newArrayList();
        String key1 = "ssss:1234";
        String key2 = "ssss:2222";
        String key3 = "ssss:3333";
        list.add(key1);
        list.add(key2);
        list.add(key3);
        list.forEach(key -> {
            boolean islock = redisLock.lock(key, "123we456rt", 1, TimeUnit.DAYS, 1, 100L);
            System.out.println("################islock:" + islock);
            String s = redisUtils.get(key);
            System.out.println("****************value:" + s);
        });

        redisLock.releaseLock(list, "123we456rt");
        return "成功";
    }

}
