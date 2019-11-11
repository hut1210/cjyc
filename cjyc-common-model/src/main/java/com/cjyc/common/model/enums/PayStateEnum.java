package com.cjyc.common.model.enums;

/**
 * 结算状态枚举
 * @author JPG
 */
public enum PayStateEnum {

    /**结算方式 0到付（默认），1预付，2账期*/
    UNPAID("未支付", 0),
    PAID("账期", 2);

    public String name;
    public int code;

    PayStateEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
}
