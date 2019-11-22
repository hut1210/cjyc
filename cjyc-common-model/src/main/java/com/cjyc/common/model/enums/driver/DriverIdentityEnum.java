package com.cjyc.common.model.enums.driver;

/**
 * @Description 司机身份枚举类
 * @Author Liu Xing Xiang
 * @Date 2019/11/22 9:23
 **/
public enum DriverIdentityEnum {
    /***/
    GENERAL_DRIVER("普通司机", 0),
    CARRIER_MANAGER("管理员", 1);

    public String name;
    public int code;

    DriverIdentityEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
}
