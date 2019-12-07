package com.cjyc.common.model.enums.customer;

/**
 * 客户状态枚举
 * @author JPG
 */
public enum CustomerStateEnum {

    /**0待审核，1审核中，2已审核，7已冻结*/
    WAIT_LOGIN("待审核", 0),
    WAIT_CHECK("审核中",1),
    CHECKED("已审核", 2),
    REJECT("审核拒绝", 3),
    FROZEN("已冻结", 7);

    public String name;
    public int code;

    CustomerStateEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
}
