package com.cjyc.common.model.enums.Coupon;

public enum CouponTypeEnum {

    FULL_REDUCTION("满减", 0),
    DIRECT_REDUCTION("直减", 1),
    DISCOUNT("折扣", 2);

    public String name;
    public int code;


    CouponTypeEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
}
