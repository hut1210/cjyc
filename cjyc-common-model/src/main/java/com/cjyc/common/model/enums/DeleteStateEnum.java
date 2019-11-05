package com.cjyc.common.model.enums;

public enum DeleteStateEnum {

    NO_DELETE("未删除",0),
    YES_DELETE("删除",1);

    public String name;
    public int code;

    DeleteStateEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
}
