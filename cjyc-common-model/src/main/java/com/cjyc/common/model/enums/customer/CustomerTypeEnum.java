package com.cjyc.common.model.enums.customer;


/**
 *  客户类型枚举
 * @author JPG
 */
public enum CustomerTypeEnum {
    /***/
    INDIVIDUAL("个人", 1),
    ENTERPRISE("企业人员", 2),
    COOPERATOR("合伙人",3);

    public String name;
    public int code;

    CustomerTypeEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
}
