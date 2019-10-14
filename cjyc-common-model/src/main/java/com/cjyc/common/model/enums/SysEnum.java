package com.cjyc.common.model.enums;

/**
 *  @author: zj
 *  @Date: 2019/10/12 10:56
 *  @Description: 韵车2.0表字段状态
 */
public enum SysEnum {

    ZERO("0"), ONE("1"),TWO("2"),FIVE("5"),TEN("10"),FIFTEEN("15"),TWENTY_FIVE("25"),
    FIFTY_FIVE("55"),EIGHTY_EIGHT("88"),ONE_HUNDRED("100");

    private String value;

    SysEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
