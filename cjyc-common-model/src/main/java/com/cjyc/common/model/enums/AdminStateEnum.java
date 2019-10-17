package com.cjyc.common.model.enums;

/**
 * 管理员状态枚举
 * @author JPG
 */
public enum AdminStateEnum {
    /***/
    WAIT_CHECK("审核中", 0),
    CHECKED("在职", 2),
    CANCEL("取消审核", 4),
    REJECTED("已驳回", 7),
    LEAVE("已离职", 9);

    public String name;
    public int code;

    AdminStateEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }}
