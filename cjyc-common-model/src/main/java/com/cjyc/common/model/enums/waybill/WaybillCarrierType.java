package com.cjyc.common.model.enums.waybill;

public enum WaybillCarrierType {

    /***/
    CARRIER("承运商", 0),
    ADMIN("业务员", 1),
    SELF("客户自己", 2);

    public String name;
    public int code;

    WaybillCarrierType(String name, int code) {
        this.name = name;
        this.code = code;
    }
}
