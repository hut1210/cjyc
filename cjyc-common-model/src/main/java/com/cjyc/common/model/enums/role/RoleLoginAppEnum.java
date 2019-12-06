package com.cjyc.common.model.enums.role;

/**
 * 角色登录APP
 */
public enum RoleLoginAppEnum {
    CAN_LOGIN_APP("可以登录APP", 1), CAN_NOT_LOGIN_APP("不可登录APP", 2);
    private RoleLoginAppEnum(String name, int flag) {
        this.name = name;
        this.flag = flag;
    }
    private String name;
    private int flag;
    public String getName() {
        return name;
    }

    public int getFlag() {
        return flag;
    }
}
