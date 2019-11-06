package com.cjyc.web.api.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 统计耗时
 * @author JPG
 */
@Aspect
@Slf4j
@Component
public class TimeInterceptor {
    //@Around(value = "execution (* com.cjyc.web.api.service..*.*(..))")
    public Object timeAround(ProceedingJoinPoint joinPoint) {
        // 定义返回对象、得到方法需要的参数  
        Object obj = null;
        Object[] args = joinPoint.getArgs();
        long startTime = System.currentTimeMillis();

        try {
            obj = joinPoint.proceed(args);
        } catch (Throwable e) {
            log.error("统计方法执行耗时环绕通知出错", e);
        }
        // 获取执行的方法名
        long endTime = System.currentTimeMillis();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getDeclaringTypeName() + "." + signature.getName();
        // 打印耗时的信息  
        this.printExecTime(methodName, startTime, endTime);
        return obj;
    }

    /**
     * 打印方法执行耗时的信息，如果超过了一定的时间，才打印
     *
     * @param methodName 方法名
     * @param startTime 开始时间
     * @param endTime 结束时间
     */
    private void printExecTime(String methodName, long startTime, long endTime) {
        long diffTime = endTime - startTime;
        if (diffTime > 100) {
            log.error("【耗时统计】方法名称：{} \t 方法执行耗时：{} ms", methodName, diffTime);
        }
    }
}
