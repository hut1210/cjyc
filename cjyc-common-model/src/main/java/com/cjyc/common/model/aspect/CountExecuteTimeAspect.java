package com.cjyc.common.model.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * 运行时间统计
 * @author JPG
 */
@Aspect
@Slf4j
@Component
public class CountExecuteTimeAspect {
    //public static final String POINTCUT = "execution (* com.cjyc.api.service..*.*(..))";
    public static final long TIME_THRESHOLD = 1000;

    @Pointcut("@annotation(com.cjyc.common.model.annotation.CountExecuteTime)")
    public void pointcut(){

    }

    @Around("pointcut()")
    public Object timeAround(ProceedingJoinPoint joinPoint) {  
        Object obj = null;
        Object[] args = joinPoint.getArgs();  
        long startTime = System.currentTimeMillis();  
  
        try {  
            obj = joinPoint.proceed(args);  
        } catch (Throwable e) {  
            log.error("统计某方法执行耗时环绕通知出错", e);
        }  
        long endTime = System.currentTimeMillis();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();  
        String methodName = signature.getDeclaringTypeName() + "." + signature.getName();  
        this.printExecTime(methodName, startTime, endTime);
        return obj;  
    }

    private void printExecTime(String methodName, long startTime, long endTime) {
        long diffTime = endTime - startTime;  
        if(diffTime > TIME_THRESHOLD){
        	 log.info("【慢操作】方法名称：{} \t 方法执行耗时：{} ms",methodName,diffTime);
        }
    }  
}
