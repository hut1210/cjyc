package com.cjyc.common.model.enums;
/**
 * 发号枚举
 * @author JPG
 */
public enum SendNoTypeEnum {
    /***/
    ORDER("订单编号","D", 5),
    WAYBILL("运单编号","Y", 6),
    CUSTOMER("客户编号","KH",8),
    DRIVER("司机编号","SJ", 8),
    RECEIPT("应收流水号","S", 6),
    PAYMENT("应付流水号","F", 6),
    COUPON("优惠券编号","Q",4);

    public String name ;
    public String prefix ;
    public int randomLength ;

    SendNoTypeEnum(String name, String prefix, int randomLength) {
        this.name = name;
        this.prefix = prefix;
        this.randomLength = randomLength;
    }
}
