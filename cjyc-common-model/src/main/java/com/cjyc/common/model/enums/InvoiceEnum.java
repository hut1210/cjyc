package com.cjyc.common.model.enums;

public enum InvoiceEnum {

    //是否开发票 0：否  1：是
    NO_INVOICE("不开票",0),
    YES_INVOICE("开票",1);

    public String name;
    public int code;

    InvoiceEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
}
