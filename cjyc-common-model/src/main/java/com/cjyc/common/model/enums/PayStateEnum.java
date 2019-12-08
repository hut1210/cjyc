package com.cjyc.common.model.enums;

/**
 * 结算状态枚举
 * @author JPG
 */
public enum PayStateEnum {

    /**交易状态： 0未支付，1支付中， 2支付成功，3支付超时，4支付失败*/
    UNPAID("未支付", 0),
    PAYING("支付中", 1),
    PAID("已支付", 2),
    TIMEOUT("支付超时", 3),
    FAIL("支付失败", 4);

    public String name;
    public int code;

    PayStateEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
}
