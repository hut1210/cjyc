package com.cjyc.customer.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * api版本注解
 * Created by leo on 2019/7/25.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ApiVersion {
    /**
     * 接口版本号(对应swagger中的group)
     * @return String[]
     */
    String[] group();
}
