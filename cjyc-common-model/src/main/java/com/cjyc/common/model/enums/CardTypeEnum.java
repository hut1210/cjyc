package com.cjyc.common.model.enums;

public enum CardTypeEnum {

    PUBLIC("对公", 1),
    PRIVATE("对私", 2);

    public String name;
    public int code;

    CardTypeEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
}
