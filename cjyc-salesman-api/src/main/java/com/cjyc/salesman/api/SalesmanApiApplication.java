package com.cjyc.salesman.api;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动类
 * @author JPG
 * @since 2019/7/19 17:14
 */
@SpringBootApplication
@MapperScan("com.cjyc.common.model.dao")
public class SalesmanApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(SalesmanApiApplication.class, args);
    }
}
