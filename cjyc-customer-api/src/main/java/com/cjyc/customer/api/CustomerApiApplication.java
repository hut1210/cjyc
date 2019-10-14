package com.cjyc.customer.api;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * 启动类
 * @Author JPG
 * @Date 2019/7/19 17:14
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan("com.cjyc.common.model.dao")
@ComponentScan({"com.cjyc.customer.api","com.cjyc.common.service"})
public class CustomerApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(CustomerApiApplication.class, args);
    }
}
