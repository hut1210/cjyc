package com.cjyc.common.model.enums.coupon;

public enum CouponTypeEnum {

    FULL_CUT("满减",0),
    DIRECT_CUT("直减",1),
    DISCOUNT_CUT("折扣",2);

    public String name;
    public int code;

    CouponTypeEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
}
