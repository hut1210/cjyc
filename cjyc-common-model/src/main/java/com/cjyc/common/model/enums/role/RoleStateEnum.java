package com.cjyc.common.model.enums.role;

/**
 * 角色状态
 */
public enum RoleStateEnum {
    VALID("有效/开启", 1), UN_VALID("无效/停用", 2);
    private RoleStateEnum(String name, int state) {
        this.name = name;
        this.state = state;
    }
    private String name;
    private int state;

    public String getName() {
        return name;
    }

    public int getState() {
        return state;
    }
}
