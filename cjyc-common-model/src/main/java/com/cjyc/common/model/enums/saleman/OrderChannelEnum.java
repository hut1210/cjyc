package com.cjyc.common.model.enums.saleman;

public enum OrderChannelEnum {
    RECEIPT("接单", 0), ALL("全部", 1);
    private String desc;
    private int code;
    private OrderChannelEnum(String desc, int code){
        this.desc = desc;
        this.code = code;
    }
    public String getDesc() {
        return desc;
    }
    public int getCode() {
        return code;
    }
}
