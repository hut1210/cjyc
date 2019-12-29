package com.cjyc.common.model.enums.order;

/**
 * 订单变更枚举
 * @author JPG
 */
public enum OrderChangeTypeEnum {

    /***/
    CHANGE_FEE("订单改价", 1),
    CHANGE_ORDER("修改订单", 2),
    REPLENISH_INFO("完善信息", 3),
    CANCEL("取消订单", 4),
    REJECT("驳回订单", 5),
    OBSOLETE("作废订单", 7);

    public String name;
    public int code;

    OrderChangeTypeEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }}
