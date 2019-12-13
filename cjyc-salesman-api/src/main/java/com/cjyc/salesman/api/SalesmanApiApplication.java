package com.cjyc.salesman.api;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 启动类
 * @author JPG
 * @since 2019/7/19 17:14
 */
@SpringBootApplication
@EnableAsync
@EnableDiscoveryClient
@EnableFeignClients({"com.cjyc.common.system.feign"})
@MapperScan("com.cjyc.common.model.dao")
@ComponentScan({"com.cjyc.salesman.api", "com.cjkj.common.redis","com.cjyc.common.system"})
public class SalesmanApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(SalesmanApiApplication.class, args);
    }
}
