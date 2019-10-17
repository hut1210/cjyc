package com.cjyc.common.model.enums;

/**
 * 业务中心数据权限范围枚举
 * @author JPG
 */
public enum BizScopeEnum {

    /***/
    NONE("无数据权限", 0),
    ALL("全部数据权限", 1),
    STORE("业务中心数据权限", 2);

    public String name;
    public int code;

    BizScopeEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
}
