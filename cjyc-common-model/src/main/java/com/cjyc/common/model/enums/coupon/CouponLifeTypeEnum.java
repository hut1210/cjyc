package com.cjyc.common.model.enums.coupon;

/**
 * 优惠券有效期
 * @author JPG
 */
public enum CouponLifeTypeEnum {

    /***/
    FOREVER("永久",1),
    PERIOD("固定时间段",2),
    DAYS("固定天数",3);

    public String name;
    public int code;

    CouponLifeTypeEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
}
