package com.cjyc.common.model.enums.transport;

public enum ModeTypeEnum {

    TIME("时付", 0),
    PERIOD("账期", 1);

    public String name;
    public int code;

    ModeTypeEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
}
