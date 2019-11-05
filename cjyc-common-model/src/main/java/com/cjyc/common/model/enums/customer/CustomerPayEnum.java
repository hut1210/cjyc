package com.cjyc.common.model.enums.customer;

public enum CustomerPayEnum {
    /***/
    TIME_PAY("时付", 0),
    PERIOD_PAY("账期", 1);

    public String name;
    public int code;

    CustomerPayEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
}
