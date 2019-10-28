package com.cjyc.common.model.enums.transport;

public enum DriverIdentityEnum {

    //司机身份 0：普通司机 1：管理员 2：超级管理员
    COMMON_DRIVER("普通司机", 0),
    ADMIN("管理员", 1),
    SUPERADMIN("超级管理员", 2);

    public String name;
    public int code;

    DriverIdentityEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }

}
