package com.cjyc.customer.api.controller;

import com.cjyc.common.model.dao.IOrderCarDao;
import com.cjyc.common.system.util.RedisLock;
import com.cjyc.common.system.util.RedisUtils;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@RestController
@Api(tags = "test")
public class testController {
    @Autowired
    RedisLock redisLock;
    @Autowired
    RedisUtils redisUtils;
    @Resource
    private IOrderCarDao orderCarDao;
    @GetMapping("/test/ds")
    public String test(){
        redisLock.lock("we", "1111111",1, TimeUnit.MINUTES, 1, 100L);
        System.out.println("解锁开始："  + new Date());
        redisLock.delayReleaseLock("we", "1111111");
        System.out.println("解锁结束：" + new Date());
        return null;
    }

}
