package com.cjyc.common.model.enums;

public enum MapEnum {

    CODE("code", 1),
    NAME("name", 2);

    public String name;
    public int code;

    MapEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
}
