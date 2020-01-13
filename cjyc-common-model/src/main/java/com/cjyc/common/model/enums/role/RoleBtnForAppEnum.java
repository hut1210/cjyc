package com.cjyc.common.model.enums.role;

/**
 * 业务员APP按钮枚举
 */
public enum RoleBtnForAppEnum {
    ALLOCATE_ORDER("分配订单", 1), PICK_DISPATCH("提车调度", 2),
    TRUNK_DISPATCH("干线调度", 3), SEND_DISPATCH("送车调度", 4);
    RoleBtnForAppEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
    public String name;
    public int code;
}
