package com.cjyc.driver.api;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by litan on 2019/9/27.
 */
@SpringBootApplication
@MapperScan({"com.cjyc.driver.api.dao","com.cjyc.common.model"})
@ComponentScan({"com.cjyc.driver.api","com.cjyc.common.model","com.cjyc.common.service"})
public class DriverApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(DriverApiApplication.class, args);
    }
}
