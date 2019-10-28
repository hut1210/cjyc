package com.cjyc.common.model.enums.transport;

public enum DriverDepositEnum {

    //是否缴纳保证金：0否，1是
    NO_DEPOSIT("否", 0),
    YES_DEPOSIT("是", 1);

    public String name;
    public int code;

    DriverDepositEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
}
