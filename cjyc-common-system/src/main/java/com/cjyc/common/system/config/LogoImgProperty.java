package com.cjyc.common.system.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogoImgProperty {

    public static String logoImg;
    public static String apiKey;
    public static String url;

    @Value("${cjyc.carSeries.logoImg}")
    public void setLogoImg(String logoImg) {
        LogoImgProperty.logoImg = logoImg;
    }

    @Value("${cjyc.carSeries.apiKey}")
    public void setApiKey(String apiKey) {
        LogoImgProperty.apiKey = apiKey;
    }

    @Value("${cjyc.carSeries.url}")
    public void setUrl(String url) {
        LogoImgProperty.url = url;
    }
}