package com.cjyc.common.model.enums.transport;

public enum DriverTypeEnum {

    OWN("自营司机", 1),
    SOCIETY("社会司机", 2);

    public String name;
    public int code;

    DriverTypeEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
}
