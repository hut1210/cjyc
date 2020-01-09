package com.cjyc.common.model.enums;

public enum InvoiceTypeEnum {

    //发票类型 1-普通(个人) ，2-增值普票(企业) ，3-增值专用发票'
    PERSONAL_INVOICE("普通(个人)",1),
    COMPANY_INVOICE("增值普票(企业)",2),
    SPECIAL_INVOICE("增值专用发票",3);

    public String name;
    public int code;

    InvoiceTypeEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
}
