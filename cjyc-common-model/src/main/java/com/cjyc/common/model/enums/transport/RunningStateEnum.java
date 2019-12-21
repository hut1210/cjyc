package com.cjyc.common.model.enums.transport;

public enum RunningStateEnum {
    EFFECTIVE("有效", 1),
    NO_EFFECTIVE("无效", 0);

    public String name;
    public int code;

    RunningStateEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
}
