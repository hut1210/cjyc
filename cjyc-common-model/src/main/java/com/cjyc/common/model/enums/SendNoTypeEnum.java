package com.cjyc.common.model.enums;
/**
 * 发号枚举
 * @author JPG
 */
public enum SendNoTypeEnum {
    /***/
    ORDER("订单编号","D"),
    WAYBILL("运单编号","Y"),
    TASK("任务编号","R"),
    COUPON("任务编号","Q");

    public String name ;
    public String prefix ;

    SendNoTypeEnum(String name, String prefix) {
        this.name = name;
        this.prefix = prefix;
    }
}
