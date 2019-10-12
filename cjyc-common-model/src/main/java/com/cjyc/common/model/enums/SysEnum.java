package com.cjyc.common.model.enums;

/**
 *  @author: zj
 *  @Date: 2019/10/12 10:56
 *  @Description: 韵车2.0表字段状态
 */
public enum SysEnum {

    ZERO(0), ONE(1);

    private Integer value;

    SysEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

}
