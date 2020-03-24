package com.cjyc.foreign.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author:Hut
 * @Date:2019/12/04 15:10
 */
@Configuration
public class ThreadPoolConfig {
    @Bean
    public ExecutorService getThreadPool(){
        return Executors.newFixedThreadPool(5);
    }
}
