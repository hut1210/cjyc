package com.cjyc.common.model.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Header参数校验注解
 *
 * Created by leo on 2019/7/25.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface HeaderIgnoreNav {
    /**
     * 是否过滤token校验
     * <p>注意：需要校验token的不需添加此注解</p>
     * */
    boolean tokenIgnore() default true;

    /**
     * 是否过滤id校验
     * <p>注意：需要校验token的不需添加此注解</p>
     * */
    boolean idIgnore() default true;

}
