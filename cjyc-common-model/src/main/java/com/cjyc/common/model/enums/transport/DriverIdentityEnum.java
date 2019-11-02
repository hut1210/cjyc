package com.cjyc.common.model.enums.transport;

public enum DriverIdentityEnum {

    //司机身份 0：个人司机 1：下属司机 2：管理员 3：超级管理员
    PERSONAL_DRIVER("个人司机", 0),
    SUB_DRIVER("下属司机", 1),
    ADMIN("管理员", 2),
    SUPERADMIN("超级管理员", 3);

    public String name;
    public int code;

    DriverIdentityEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }

}
