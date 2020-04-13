package com.cjyc.customer.api.controller;

import com.alibaba.fastjson.JSON;
import com.cjyc.common.model.dao.IOrderCarDao;
import com.cjyc.common.system.util.RedisLock;
import com.cjyc.common.system.util.RedisUtils;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

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
        Map<Object, Object> map = orderCarDao.findVinListByOrderNo("D20011355643");
        System.out.println(JSON.toJSONString(map));
        return JSON.toJSONString(map);
    }

}
