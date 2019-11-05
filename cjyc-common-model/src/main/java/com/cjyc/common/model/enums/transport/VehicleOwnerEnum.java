package com.cjyc.common.model.enums.transport;

public enum VehicleOwnerEnum {

    SELFBUSINESS("韵车自营", 0),
    PERSONAL("个人所有", 1),
    CARRIER("承运商",2);

    public String name;
    public int code;

    VehicleOwnerEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
}
