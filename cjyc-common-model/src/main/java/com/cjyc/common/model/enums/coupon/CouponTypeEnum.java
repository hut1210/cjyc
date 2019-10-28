package com.cjyc.common.model.enums.coupon;

/**
 * 优惠券类型
 * @author JPG
 */
public enum CouponTypeEnum {
    /***/
    FULL_CUT("满减",1),
    DIRECT_CUT("直减",2),
    DISCOUNT_CUT("折扣",3);

    public String name;
    public int code;

    CouponTypeEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
}
