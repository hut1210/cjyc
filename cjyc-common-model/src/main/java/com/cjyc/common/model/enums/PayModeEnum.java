package com.cjyc.common.model.enums;

/**
 * 结算方式枚举
 * @author JPG
 */
public enum PayModeEnum {

    /**结算方式 0到付（默认），1预付，2账期*/
    COLLECT("到付", 0),
    PREPAY("预付", 1),
    PERIOD("账期", 2);

    public String name;
    public int code;

    PayModeEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
}
