package com.cjyc.common.model.enums.task;

/**
 * 任务状态枚举
 * @author JPG
 */
public enum TaskStateEnum {
    /**0待承接，5待装车，55运输中，100已完成，113已取消，115已拒接*/
    WAIT_ALLOT_CONFIRM("待承接", 0),
    WAIT_LOAD("待装车", 5),
    TRANSPORTING("运输中", 55),
    FINISHED("已完成", 100),
    F_CANCEL("已取消", 113),
    F_REJECTED("已拒接", 115);

    public String name;
    public int code;

    TaskStateEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }}
