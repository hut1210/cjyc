package com.cjyc.driver.api;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by litan on 2019/9/27.
 */
@SpringBootApplication
@MapperScan("com.cjyc.driver.api.dao")
public class DriverApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(DriverApiApplication.class, args);
    }
}
