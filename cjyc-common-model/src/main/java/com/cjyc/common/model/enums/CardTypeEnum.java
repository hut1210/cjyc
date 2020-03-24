package com.cjyc.common.model.enums;

public enum CardTypeEnum {

    PUBLIC("公户", 1),
    PRIVATE("私户", 2);

    public String name;
    public int code;

    CardTypeEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
}
