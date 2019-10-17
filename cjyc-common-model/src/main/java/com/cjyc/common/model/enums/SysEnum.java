package com.cjyc.common.model.enums;

/**
 *  @author: zj
 *  @Date: 2019/10/12 10:56
 *  @Description: 韵车2.0表字段状态
 */
public enum SysEnum {

    ZERO("0"), ONE("1"),TWO("2"),THREE("3"),FOUR("4"),FIVE("5"),
    SIX("6"),SEVEN("7"),EIGHT("8"),NINE("9"),TEN("10");

    private String value;

    SysEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
