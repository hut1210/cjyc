package com.cjyc.common.model.enums.customer;

/**
 * @Author: Hut
 * @Date: 2020/04/07 15:03、
 *
 * 客户付款方式
 **/
public enum CustomerPayTypeEnum {
    TO_PAY("到付", 0),
    PRE_PAY("预付", 1),
    PERIOD_PAY("账期", 2);

    public String name;
    public int code;

    CustomerPayTypeEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
}
