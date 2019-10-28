package com.cjyc.common.model.enums.task;

/**
 * 任务车辆状态枚举
 * @author JPG
 */
public enum TaskCarStateEnum {
    /***/
    WAIT_LOAD("待装车", 1),
    LOADED("已装车", 2),
    UNLOADED("已卸车", 4),
    FINISHED("确认收车", 9);

    public String name;
    public int code;

    TaskCarStateEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
}
