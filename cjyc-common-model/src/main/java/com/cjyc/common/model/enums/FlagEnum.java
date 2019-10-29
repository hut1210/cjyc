package com.cjyc.common.model.enums;

public enum FlagEnum {

    //1：保存 2：更新
    QUERY("查询", 0),
    ADD("保存", 1),
    UPDTATE("更新", 2),
    AUDIT_PASS("审核通过",3),
    AUDIT_REJECT("审核拒绝",4),
    FROZEN("冻结",5),
    THAW("解冻",6),
    DELETE("删除",7);

    public String name;
    public int code;

    FlagEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
}
