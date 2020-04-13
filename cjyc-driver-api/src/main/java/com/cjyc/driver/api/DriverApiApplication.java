package com.cjyc.driver.api;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author JPG
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients({"com.cjyc"})
@MapperScan("com.cjyc.common.model.dao")
@ComponentScan({"com.cjyc.driver.api", "com.cjyc.common.system","com.cjkj.common.redis"})
public class DriverApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(DriverApiApplication.class, args);
    }
}