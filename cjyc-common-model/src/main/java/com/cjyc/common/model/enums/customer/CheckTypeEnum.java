package com.cjyc.common.model.enums.customer;

public enum CheckTypeEnum {

    /**审核类型*/
    UPGRADE_PARTNER("C端用户升级合伙人", 0);

    public String name;
    public int code;

    CheckTypeEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
}
