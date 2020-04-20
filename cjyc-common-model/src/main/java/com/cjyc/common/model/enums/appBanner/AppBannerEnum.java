package com.cjyc.common.model.enums.appBanner;

public enum AppBannerEnum {
    /**
     * 客户端轮播图
     */
    CUSTOMER_BANNER("客户端轮播图", "system_picture_customer"),
    DRIVER_BANNER("司机端轮播图", "system_picture_driver");

    public String name;
    public String item;

    AppBannerEnum(String name, String item) {
        this.name = name;
        this.item = item;
    }
}
