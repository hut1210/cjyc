package com.cjyc.customer.api.annotations;

import java.lang.annotation.*;

/**
 * 接口调用信息日志注解
 * 添加到需要记录日志信息的方法上
 * Created by leo on 2019/7/26
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface OperationLogNav {
    String value() default "";
}
