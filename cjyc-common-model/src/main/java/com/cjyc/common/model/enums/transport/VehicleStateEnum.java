package com.cjyc.common.model.enums.transport;

public enum VehicleStateEnum {

    INVALID("无效", 0),
    EFFECTIVE("有效", 1);

    public String name;
    public int code;

    VehicleStateEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
}
