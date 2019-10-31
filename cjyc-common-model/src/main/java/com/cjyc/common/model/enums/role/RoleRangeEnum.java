package com.cjyc.common.model.enums.role;

/**
 * 创建角色所属机构范围
 */
public enum RoleRangeEnum {
    INNER("内部机构", 1), OUTER("外部机构", 2);
    private String range;
    private int value;
    private RoleRangeEnum(String range, int value) {
        this.range = range;
        this.value = value;
    }

    public String getRange() {
        return range;
    }

    public int getValue() {
        return value;
    }
}
