package com.cjyc.web.api;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by DELL on 2019/9/27.
 */
@SpringBootApplication
@MapperScan("com.cjyc.common.model.dao")
public class WebApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebApiApplication.class, args);
    }
}
