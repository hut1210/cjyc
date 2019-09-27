package com.cjyc.customer.api;

import org.mybatis.spring.annotation.MapperScan;

/**
 * 启动类
 * @Author JPG
 * @Date 2019/7/19 17:14
 */
@SpringBootApplication
@MapperScan("com.cjyc.customer.api.dao")
public class CustomerApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(CustomerApiApplication.class, args);
    }
}
