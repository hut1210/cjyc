package com.cjyc.common.model.enums;

public enum FlagEnum {

    //0：查询 1：保存 2：更新 3：审核通过 4：审核拒绝 5：冻结 6：解冻 7：删除 8：作废
    QUERY("查询", 0),
    ADD("保存", 1),
    UPDTATE("更新", 2),
    AUDIT_PASS("审核通过",3),
    AUDIT_REJECT("审核拒绝",4),
    FROZEN("冻结",5),
    THAW("解冻",6),
    DELETE("删除",7),
    NULLIFY("作废",8),
    TURNOFF_SWITCH("关闭开关",9),
    TURNONN_SWITCH("打开开关",10);

    public String name;
    public int code;

    FlagEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
}
