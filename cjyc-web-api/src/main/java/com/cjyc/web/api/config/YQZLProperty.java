package com.cjyc.web.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: RenPL
 * @Date: 2020/03/25 11:32
 */
@Configuration
@RefreshScope
public class YQZLProperty {

    /**
     * 企业代码
     */
    public static String corpNo;

    /**
     * 企业用户号
     */
    public static String userNo;

    @Value("${yqzl.corp_no}")
    public void setCorpNo(String corpNo) {
        YQZLProperty.corpNo = corpNo;
    }
    @Value("${yqzl.user_no}")
    public void setUserNo(String userNo) {
        YQZLProperty.userNo = userNo;
    }
}
