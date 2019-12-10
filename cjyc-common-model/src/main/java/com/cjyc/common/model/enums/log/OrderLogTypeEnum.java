package com.cjyc.common.model.enums.log;

public enum OrderLogTypeEnum {

    RECEIPT("签收", 100);

    private String name;
    private int code;

    OrderLogTypeEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public int getCode() {
        return code;
    }
}
