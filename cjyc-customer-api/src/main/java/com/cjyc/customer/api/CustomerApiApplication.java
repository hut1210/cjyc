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
@MapperScan({"com.cjyc.customer.api.dao","com.cjyc.common.model"})
@ComponentScan({"com.cjyc.customer.api","com.cjyc.common.model","com.cjyc.common.service"})
public class CustomerApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(CustomerApiApplication.class, args);
    }
}
