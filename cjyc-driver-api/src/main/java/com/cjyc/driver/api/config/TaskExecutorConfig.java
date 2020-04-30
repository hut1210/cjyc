package com.cjyc.driver.api.config;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * 异步任务配置类，为防止异步任务如下错误：
 * No qualifying bean of type 'org.springframework.core.task.TaskExecutor' available:
 * expected single matching bean but found 2: applicationTaskExecutor,taskScheduler
 **/
@Configuration
@ComponentScan("com.*.*.*.service")
public class TaskExecutorConfig implements AsyncConfigurer {

    /**
     * 实现AsyncConfigurer接口并重写getAsyncExecutor方法，
     * 并返回一个ThreadPoolTaskExecutor，这样我们就获得了一个基于线程池TaskExecutor
     **/
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(10);
        taskExecutor.setMaxPoolSize(80);
        taskExecutor.setQueueCapacity(100);
        taskExecutor.initialize();
        return taskExecutor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return null;
    }
}