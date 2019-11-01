package com.cjyc.common.model.enums.waybill;

/**
 * 运单状态枚举
 * @author JPG
 */
public enum WaybillStateEnum {
    /**0待分配承运商（竞抢），15待承运商承接任务，55运输中，100已完成，111超时关闭，113已取消，115已拒接*/
    WAIT_ALLOT("待分配承运商（竞抢）",0),
    WAIT_ALLOT_CONFIRM("待承运商承接任务",15),
    ALLOT_CONFIRM("已承接",20),
    TRANSPORTING("运输中",55),
    FINISHED("已完成",100),
    F_TIMEOUT("超时关闭",111),
    F_CANCEL("已取消",113),
    F_REJECTED("已拒接",115);

    public String name;
    public int code;

    WaybillStateEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
}
