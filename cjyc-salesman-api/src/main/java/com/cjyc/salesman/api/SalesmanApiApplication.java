package com.cjyc.salesman.api;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 启动类
 * @author JPG
 * @since 2019/7/19 17:14
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan("com.cjyc.common.model.dao")
public class SalesmanApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(SalesmanApiApplication.class, args);
    }
}
