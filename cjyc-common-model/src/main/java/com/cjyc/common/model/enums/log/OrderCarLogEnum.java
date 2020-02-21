package com.cjyc.common.model.enums.log;

/**
 * 轨迹日志枚举
 * @author JPG
 */
public enum OrderCarLogEnum {

    /***/
    C_LOAD("已装车", "车辆，已在【{0}】装车", "车辆，已在【{0}】装车，司机【{1}/{2}】", 21),
    C_UNLOAD("已卸车", "车辆，已在【{0}】卸车", "车辆, 已在【{0}】卸车，司机【{1}/{2}】", 22),
    C_UNLOAD_MIDWAY("已卸载", "车辆，已在【{0}】卸载", "车辆，已在【{0}】卸载，操作人【{1}/{2}】", 23),
    C_IN_STORE("已入库", "车辆，已在【{0}】入库", "车辆，已在【{0}】入库，操作人【{1}/{2}】", 24),
    C_OUT_STORE("已出库", "车辆，已在【{0}】出库", "车辆, 已在【{0}】出库，操作人【{1}/{2}】", 25),
    C_PAID("已支付", "车辆, 已支付", "车辆, 已收款，操作人【{1}/{2}】", 28),
    C_RECEIPT("已收车", "车辆, 已确认收车", "车辆, 已确认收车，司机【{1}/{2}】", 29);


    private String name;
    private String outterLog;
    private String innerLog;
    private int code;

    OrderCarLogEnum(String name, String outterLog, String innerLog, int code) {
        this.name = name;
        this.outterLog = outterLog;
        this.innerLog = innerLog;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public String getInnerLog() {
        return innerLog;
    }

    public String getOutterLog() {
        return outterLog;
    }

    public int getCode() {
        return code;
    }}
