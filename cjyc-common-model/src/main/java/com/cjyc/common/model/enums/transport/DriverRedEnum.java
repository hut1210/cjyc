package com.cjyc.common.model.enums.transport;

public enum DriverRedEnum {

    NORED("不标红", 0),
    RED("浅红色底色", 1);

    public String name;
    public int code;

    DriverRedEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
}
