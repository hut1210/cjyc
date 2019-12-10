package com.cjyc.common.system.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogoImgProperty {

    public static String logoImg;

    @Value("${cjyc.carSeries.logoImg}")
    public void setLogoImg(String logoImg) {
        LogoImgProperty.logoImg = logoImg;
    }
}