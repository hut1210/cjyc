package com.cjyc.common.model.enums;

/**
 * 调度类型枚举
 * @author JPG
 */
public enum WaybillTypeEnum {
    /***/
    PICK("提车任务", 1),
    BACK("配送任务", 2),
    TRUNK("干线任务", 8);

    public String name;
    public int code;

    WaybillTypeEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
}
