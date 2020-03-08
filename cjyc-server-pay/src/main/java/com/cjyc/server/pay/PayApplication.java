package com.cjyc.server.pay;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Author: Hut
 * @Date: 2020/03/08 9:47
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.cjyc.common.model.dao")
@EnableFeignClients({"com.cjyc"})
@ComponentScan({"com.cjyc.server.pay", "com.cjyc.common.system","com.cjkj.common.redis"})
public class PayApplication {
    public static void main(String[] args) {
        SpringApplication.run(PayApplication.class, args);
    }
}
