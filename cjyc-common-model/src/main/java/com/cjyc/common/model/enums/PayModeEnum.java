package com.cjyc.common.model.enums;

/**
 * 结算方式枚举
 * @author JPG
 */
public enum PayModeEnum {

    /**结算方式 0：时付  1：账期*/
    CURRENT("时付", 0),
    PERIOD("账期", 1);

    public String name;
    public int code;

    PayModeEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
}
