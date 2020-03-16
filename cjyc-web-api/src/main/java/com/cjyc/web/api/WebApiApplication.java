package com.cjyc.web.api;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Created by DELL o5n 2019/9/27.
 */
@SpringBootApplication
@EnableAsync
@EnableRabbit
@EnableDiscoveryClient
@EnableFeignClients({"com.cjyc.common.system.feign"})
@MapperScan("com.cjyc.common.model.dao")
@ComponentScan({"com.cjyc.web.api","com.cjkj.common.redis","com.cjyc.common.system"})
public class WebApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebApiApplication.class, args);
    }
}
