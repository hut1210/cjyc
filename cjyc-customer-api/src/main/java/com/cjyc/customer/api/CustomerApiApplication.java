package com.cjyc.customer.api;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 启动类
 * @Author JPG
 * @Date 2019/7/19 17:14
 */
@SpringBootApplication
@MapperScan("com.cjyc.customer.api.dao")
@ComponentScan({"com.cjyc.common.service","com.cjyc.customer.api"})
public class CustomerApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(CustomerApiApplication.class, args);
    }
}
