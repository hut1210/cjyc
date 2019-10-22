package com.cjyc.common.model.enums.customer;


/**
 * 客户来源枚举
 * @author JPG
 */
public enum CustomerSourceEnum {
    /**
     * 1App注册，2Applet注册，3业务员创建，4企业管理员创建，5合伙人创建
     */
    APP("App注册", 0),
    APPLET("Applet注册", 1),
    CREATE_BY_SALEMAN("业务员创建", 2),
    CREATE_BY_ENTERPRISE("企业管理员创建", 3),
    CREATE_BY_COOPERATOR("合伙人创建", 4);

    public String name;
    public int code;

    CustomerSourceEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
}
