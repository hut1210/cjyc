package com.cjyc.common.model.enums.transport;

public enum CarrierTypeEnum {

    //承运商类型：1个人承运商，2企业承运商
    PERSONAL("个人承运商", 1),
    ENTERPRISE("企业承运商", 2);

    public String name;
    public int code;

    CarrierTypeEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
}
