package com.cjyc.common.model.enums;

/**
 * 业务中心数据权限范围枚举
 * @author JPG
 */
public enum BizScopeEnum {

    /***/
    NONE("无数据权限", 0),
    STORE("业务中心数据权限", 2),
    CITY("行政区划数据权限", 1);

    public String name;
    public int code;

    BizScopeEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
}
