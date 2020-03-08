package com.cjyc.scheduler.finance;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Author: Hut
 * @Date: 2020/03/08 9:26
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.cjyc.common.model.dao")
@EnableFeignClients({"com.cjyc"})
@ComponentScan({"com.cjyc.scheduler.finance", "com.cjyc.common.system","com.cjkj.common.redis"})
public class FinanceApplication {
    public static void main(String[] args) {
        SpringApplication.run(FinanceApplication.class, args);
    }
}
