package com.cjyc.common.model.enums.inquiry;

public enum InquiryStateEnum {

    NO_HANDLE("未处理", 1),
    YES_HANDLE("已处理", 2),
    NO_RED("不标红", 1),
    YES_RED("标红", 2);

    public String name;
    public int code;

    InquiryStateEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
}
