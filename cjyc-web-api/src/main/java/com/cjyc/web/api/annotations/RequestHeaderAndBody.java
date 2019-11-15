package com.cjyc.web.api.annotations;

import java.lang.annotation.*;
/**
 * 参数注解
 * @author JPG
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestHeaderAndBody {

    boolean required() default true;
}
