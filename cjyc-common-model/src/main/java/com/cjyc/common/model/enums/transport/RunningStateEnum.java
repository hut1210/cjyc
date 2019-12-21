package com.cjyc.common.model.enums.transport;

public enum RunningStateEnum {

    NO_EFFECTIVE("无效", 0),
    EFFECTIVE("有效", 1);

    public String name;
    public int code;

    RunningStateEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
}
