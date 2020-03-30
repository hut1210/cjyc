package com.cjyc.scheduler.biz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 启动类
 * @author JPG
 * @since 2020/3/30 9:42
 */
@SpringBootApplication
@EnableDiscoveryClient
public class BizSchedulerApplication {
    public static void main(String[] args) {
        SpringApplication.run(BizSchedulerApplication.class, args);
    }
}
