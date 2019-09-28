package com.cjyc.customer.api.aspect;

import com.cjkj.common.utils.IPUtil;
import com.cjyc.common.entity.OperationLog;
import com.cjyc.common.service.ILogService;
import com.cjyc.common.util.DateUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.UUID;

/**
 * 请求方法日志记录切面
 * @author leo
 * @date 2019/7/26.
 */
@Aspect
@Order(5)
@Component
public class AppOperationLogAspect {

    @Autowired
    private ILogService logService;

    //保存线程共享变量，开始执行时间
    ThreadLocal<Long> startTime = new ThreadLocal<>();
    //日志ID
    ThreadLocal<String> logId = new ThreadLocal<>();

    //定义一个切入点，匹配带有@OperationLogNav注解的方法
    @Pointcut("@annotation(com.cjyc.customer.api.annotations.OperationLogNav)")
    private void storyOperationLog() {}

    /**
     * 环绕通知
     * @param joinPoint
     */
    @Around("storyOperationLog()")
    public Object advice(ProceedingJoinPoint joinPoint) {
        Object o = null;
        try {
            o = joinPoint.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return o;
    }

    /**
     * 在请求的方法执行前执行
     * @param joinPoint
     * @throws Throwable
     */
    @Before("storyOperationLog() && @annotation(com.cjyc.customer.api.annotations.OperationLogNav)")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        //记下请求起始时间
        startTime.set(System.currentTimeMillis());
        //生成logid
        logId.set(UUID.randomUUID().toString());

        final String methodType = request.getMethod();  //请求类型
        final String url = request.getRequestURL().toString();
        final String uri = request.getRequestURI();
        final String ip = IPUtil.getIpAddr(request);
        final String account = "13012297251";
        final String token = "token";
        final String clazz = joinPoint.getSignature().getDeclaringTypeName();
        final String methodName = joinPoint.getSignature().getName();
        final Object[] args = joinPoint.getArgs();
        final String params = Arrays.toString(args);
        logService.recordLog(new OperationLog(logId.get(),token,account, ip, methodType, url, uri, clazz, methodName,
                DateUtils.getNowDateStr(), params));
    }

    /**
     * 在方法执行后环绕结束后执行
     */
    @After("storyOperationLog()")
    public void doAfter() {
        // 计算出本次请求用时，更新到日志中
        long costTime = System.currentTimeMillis() - startTime.get();
        logService.updLog(logId.get(), costTime);
    }
}
