package com.cjyc.foreign.api;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@EnableDiscoveryClient
@MapperScan("com.cjyc.foreign.api.dao")
@ComponentScan({"com.cjyc.foreign.api", "com.cjkj.common"})
public class ForeignApplication {

    public static void main(String[] args) {
        SpringApplication.run(ForeignApplication.class, args);
    }
}
