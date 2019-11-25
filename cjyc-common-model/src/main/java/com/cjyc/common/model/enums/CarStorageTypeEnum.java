package com.cjyc.common.model.enums;

public enum CarStorageTypeEnum {
    /***/
    IN("入库", 1),
    OUT("出库", 2);

    public String name;
    public int code;

    CarStorageTypeEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
}
