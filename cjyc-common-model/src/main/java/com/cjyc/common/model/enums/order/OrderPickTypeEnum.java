package com.cjyc.common.model.enums.order;

public enum OrderPickTypeEnum {
    /***/
    SELF("自己", 1),
    PILOT("代驾", 2),
    CONSIGN("拖车", 3),
    WL("物流上门", 4),
    DISPATCH_SELF("我去提车", 4);

    public String name;
    public int code;

    OrderPickTypeEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
}
