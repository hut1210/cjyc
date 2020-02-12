package com.cjyc.common.model.enums.log;

/**
 * 轨迹日志枚举
 * @author JPG
 */
public enum OrderCarLogEnum {

    /***/
    SUBMIT("已下单", "订单，已下单", "订单，已下单，操作人【{0}】", 1),
    COMMIT("已确认", "订单，已确认", "订单，已确认，操作人【{0}】", 2),
    CHECK("已接单", "订单，已接单", "订单，已接单，操作人【{0}】", 3),
    LOAD("已装车", "车辆，已在【{0}】装车", "车辆，已在【{0}】装车，司机【{1}/{2}】", 21),
    UNLOAD("已卸车", "车辆，已在【{0}】卸车", "车辆, 已在【{0}】卸车，司机【{1}/{2}】", 22),
    UNLOAD_MIDWAY("已卸载", "车辆，已在【{0}】卸载", "车辆，已在【{0}】卸载，操作人【{1}】", 23),
    IN_STORE("已入库", "车辆，已在【{0}】入库", "车辆，已在【{0}】入库，操作人【{1}】", 23),
    OUT_STORE("已出库", "车辆，已在【{0}】出库", "车辆, 已在【{0}】出库，操作人【{1}】", 24),
    RECEIPT("已签收", "车辆, 已确认签收", "车辆, 已确认签收，司机【{1}/{2}】", 29),
    FINISH("已完成", "订单，已完成", "订单，已完成", 100),
    CANCEL("已取消", "车辆，已取消", "车辆, 已取消，操作人{0}", 113);


    private String name;
    private String innerLog;
    private String outterLog;
    private int code;

    OrderCarLogEnum(String name, String innerLog, String outterLog, int code) {
        this.name = name;
        this.innerLog = innerLog;
        this.outterLog = outterLog;
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
