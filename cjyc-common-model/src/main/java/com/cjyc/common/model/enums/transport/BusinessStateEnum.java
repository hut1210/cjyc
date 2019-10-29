package com.cjyc.common.model.enums.transport;

public enum BusinessStateEnum {

    //营运状态：0营运中，1停运中
    BUSINESS("营运中/在途", 0),
    OUTAGE("停运中/空闲", 1);

    public String name;
    public int code;

    BusinessStateEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
}
