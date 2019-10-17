package com.cjyc.common.model.enums.waybill;

/**
 * 运单车辆状态
 * @author JPG
 */
public enum WaybillCarStateEnum {
    /***/
    WAIT_APPOINT("待指派", 0),
    APPOINTED("已指派", 2),
    FINISHED("已完成", 9);

    public String name;
    public int code;

    WaybillCarStateEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
}
