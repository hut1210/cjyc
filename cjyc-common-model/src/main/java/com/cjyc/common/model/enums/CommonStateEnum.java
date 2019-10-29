package com.cjyc.common.model.enums;

public enum CommonStateEnum {

    /***/
    WAIT_CHECK("待审核", 0),
    CHECKED("已审核", 2),
    CANCEL("已取消", 4),
    REJECT("已驳回", 7),
    DISABLED("已停用", 9);

    public String name;
    public int code;

    CommonStateEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
}