package com.cjyc.common.model.enums.driver;

/**
 * 承运商结算类型
 *
 * @Author: Hut
 * @Date: 2020/04/15 15:48
 **/
public enum DriverSettleTypeEnum {

    TIME_PAY("时付", 0),
    ACCOUNT_PERIOD("账期", 1);

    public String name;
    public int code;

    DriverSettleTypeEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
}
