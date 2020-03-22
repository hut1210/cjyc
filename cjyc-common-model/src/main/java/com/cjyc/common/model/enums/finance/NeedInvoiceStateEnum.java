package com.cjyc.common.model.enums.finance;

/**
 * 是否需要开发票状态枚举
 *
 * @author RenPL
 */
public enum NeedInvoiceStateEnum {

    UNNEEDED_INVOICE("不需要开票", 0),
    NEEDED_INVOICE("需要开票", 1);

    public String name;
    public int code;

    NeedInvoiceStateEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
}
