package com.cjyc.common.model.enums.driver;

public enum DriverTypeEnum {

    /***/
    DRIVER("个人司机", 0),
    CARRIER_DRIVER("下属司机", 1),
    CARRIER_MANAGER("管理员", 2),
    CARRIER_ADMINISTRATOR("超级管理员",3);

    public String name;
    public int code;

    DriverTypeEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }


}
