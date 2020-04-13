package com.cjyc.common.model.enums.customer;

/**
 * @Author: Hut
 * @Date: 2020/04/10 14:32
 *
 * 客户类型：C端客户，企业客户，合伙人
 **/
public enum ClientTypeEnum {
    INDIVIDUAL("C端客户", 1),
    ENTERPRISE("大客户", 2),
    COOPERATOR("合伙人",3);

    public String name;
    public int code;

    ClientTypeEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
}
