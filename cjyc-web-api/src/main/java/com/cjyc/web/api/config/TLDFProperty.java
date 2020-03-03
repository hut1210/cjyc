package com.cjyc.web.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Hut
 * @Date: 2020/02/25 11:32
 */
@Configuration
@RefreshScope
public class TLDFProperty {

    public static String acctNo;
    public static String merchantId;
    public static String all_userName;
    public static String all_password;
    public static String pfxPassword;
    public static String url;
    public static String business_code;
    public static String bankacct;
    public static String pfxPath;
    public static String tltcerPath;

    @Value("${tldf.acctNo}")
    public void setAcctNo(String acctNo) {
        TLDFProperty.acctNo = acctNo;
    }
    @Value("${tldf.merchantId}")
    public void setMerchantId(String merchantId) {
        TLDFProperty.merchantId = merchantId;
    }
    @Value("${tldf.all_userName}")
    public void setAll_userName(String all_userName) {
        TLDFProperty.all_userName = all_userName;
    }
    @Value("${tldf.all_password}")
    public void setAll_password(String all_password) {
        TLDFProperty.all_password = all_password;
    }
    @Value("${tldf.pfxPassword}")
    public void setPfxPassword(String pfxPassword) {
        TLDFProperty.pfxPassword = pfxPassword;
    }
    @Value("${tldf.url}")
    public void setUrl(String url) {
        TLDFProperty.url = url;
    }
    @Value("${tldf.business_code}")
    public void setBusiness_code(String business_code) {
        TLDFProperty.business_code = business_code;
    }
    @Value("${tldf.bankacct}")
    public void setBankacct(String bankacct) {
        TLDFProperty.bankacct = bankacct;
    }
    @Value("${tldf.pfxPath}")
    public void setPfxPath(String pfxPath) {
        TLDFProperty.pfxPath = pfxPath;
    }
    @Value("${tldf.tltcerPath}")
    public void setTltcerPath(String tltcerPath) {
        TLDFProperty.tltcerPath = tltcerPath;
    }
}
