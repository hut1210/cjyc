package com.cjyc.common.model.enums.task;

/**
 * 任务状态枚举
 * @author JPG
 */
public enum TaskStateEnum {
    /***/
    WAIT_ALLOT_CONFIRM("待承接", 0),
    WAIT_LOAD("待装车", 5),
    TRANSPORTING("运输中", 10),
    FINISHED("已完成", 100),
    F_CANCEL("已取消", 102),
    F_REJECTED("已拒接", 103);

    public String name;
    public int code;

    TaskStateEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }}
