package com.cjyc.common.model.enums.customer;


/**
 * 客户来源枚举
 * @author JPG
 */
public enum CustomerSourceEnum {
    /**
     * 1App注册，2Applet注册，3WEB后台创建，4升级创建
     */
    APP("App注册", 1),
    APPLET("Applet注册", 2),
    WEB("WEB后台创建",3),
    UPGRADE("升级创建",4),
    CREATE_BY_SALEMAN("业务员创建", 5),
    CREATE_BY_ENTERPRISE("企业管理员创建", 6),
    CREATE_BY_COOPERATOR("合伙人创建", 7);

    public String name;
    public int code;

    CustomerSourceEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
}
