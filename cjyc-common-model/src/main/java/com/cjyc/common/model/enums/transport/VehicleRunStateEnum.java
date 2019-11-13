package com.cjyc.common.model.enums.transport;

public enum VehicleRunStateEnum {

    FREE("空闲", 0),
    WAY("在途", 1),
    BUSY("繁忙", 2);

    public String name;
    public int code;

    VehicleRunStateEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
}
