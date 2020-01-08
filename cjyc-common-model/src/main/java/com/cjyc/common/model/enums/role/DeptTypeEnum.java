package com.cjyc.common.model.enums.role;

public enum DeptTypeEnum {

    INNER("内部机构", 1), CARRIER("承运商", 2), CUSTOMER("客户机构",3);

    public String name;
    public int code;

    DeptTypeEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
}
