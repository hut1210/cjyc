package com.yqzl.config;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotNull;

/**
 * @Author:Hut
 * @Date:2019/11/29 15:06
 */
@Configuration
public class YqzlProperty {

    public static String corp_no;
    public static String user_no;

    @Value("${cjyc.yqzl.corp_no}")
    public void setCorp_no(String corp_no){
        YqzlProperty.corp_no = corp_no;
    }

    @Value("${cjyc.yqzl.user_no}")
    public void setUser_no(String user_no){
        YqzlProperty.user_no = user_no;
    }

}
