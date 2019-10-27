package com.cjyc.common.model.enums.transport;

public enum DriverSourceEnum {

    //账号来源：1App注册，2Applet注册，3业务员创建，4承运商管理员创建，11掌控接口，12otm接口
    APP_DRIVER("App注册", 1),
    APPLET_DRIVER("Applet注册", 2),
    SALEMAN_WEB("业务员创建",3),
    CARRIER_ADMIN("承运商管理员创建",4),
    CONTROL_INTERFACE("掌控接口",11),
    OTM_INTERFACE("otm接口",12);

    public String name;
    public int code;

    DriverSourceEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
}
