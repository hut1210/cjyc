package com.cjyc.common.model.enums;

/**
 * 用户类型枚举
 * @author JPG
 */
public enum UserTypeEnum {

    /***/
    ADMIN("业务员", 1),
    DRIVER("司机",2),
    CUSTOMER("客户",3);

    final public String name;
    final public int code;

    UserTypeEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }

    public static UserTypeEnum valueOf(int code) {
        for (UserTypeEnum userTypeEnum : values()) {
            if(userTypeEnum.code == code){
                return userTypeEnum;
            }
        }
        throw new IllegalArgumentException("No matching constant for [" + code + "]");
    }
}
