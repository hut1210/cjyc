package com.cjyc.common.model.enums;

/**
 * 用户类型枚举
 * @author JPG
 */
public enum UserTypeEnum {

    /***/
    ADMIN("管理员", 0),
    SALEMAN("业务员",1),
    DRIVER("司机",2),
    CUSTOMER("客户",3);

    public String name;
    public int code;

    UserTypeEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
}
