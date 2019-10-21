package com.cjyc.common.model.enums;


/**
 *  客户类型枚举
 * @author JPG
 */
public enum CustomerTypeEnum {
    /***/
    INDIVIDUAL("个人", 0),
    ENTERPRISE("企业人员", 1),
    COOPERATOR("合伙人",2);

    public String name;
    public int code;

    CustomerTypeEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
}
