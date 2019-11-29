package com.cjyc.common.model.enums.role;

public enum RoleNameEnum {

    COMMON("司机", 1),
    ADMINSTRATOR("普通管理员", 2),
    SUPER_ADMINSTRATOR("-", 3);

    private String name;
    private int value;

    private RoleNameEnum(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }
}
