package com.cjyc.common.model.enums.transport;

public enum DriverTypeEnum {

    //类型：1自营司机，2社会司机
    OWN_DRIVER("自营司机", 1),
    SOCIETY_DRIVER("社会司机", 2);

    public String name;
    public int code;

    DriverTypeEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
}
