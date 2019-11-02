package com.cjyc.common.model.enums.waybill;

public enum WaybillCarTurnType {

    /***/
    HOME("客户家", 1),
    MIDWAY("中途", 2),
    STORE("业务中心", 3);

    public String name;
    public int code;

    WaybillCarTurnType(String name, int code) {
        this.name = name;
        this.code = code;
    }
}
