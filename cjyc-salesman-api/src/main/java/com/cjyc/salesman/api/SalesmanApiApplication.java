package com.cjyc.salesman.api;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by DELL on 2019/9/27.
 */
@SpringBootApplication
@MapperScan("com.cjyc.salesman.api.dao")
public class SalesmanApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(SalesmanApiApplication.class, args);
    }
}
