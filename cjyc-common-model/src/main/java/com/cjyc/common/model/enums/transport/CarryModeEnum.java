package com.cjyc.common.model.enums.transport;

public enum CarryModeEnum {

    //承运方式：1代驾，2托运，3干线 4全支持
    DRIVER("代驾", 1),
    CONSIGN("托运", 2),
    TRUNK("干线", 3),
    SUPPORT("全支持",4);

    public String name;
    public int code;

    CarryModeEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }

}
