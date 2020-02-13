package com.cjyc.common.model.enums.log;

public enum OrderLogEnum {

    /***/
    SUBMIT("已下单", "订单，已下单", "订单，已下单，操作人【{0}/{1}】", 1),
    COMMIT("已确认", "订单，已确认", "订单，已确认，操作人【{0}/{1}】", 2),
    WAIT_PREPAY("待付款", "订单，已确认，等待支付运费", "订单，已确认，等待支付运费，操作人【{0}/{1}】", 3),
    CHECK("已接单", "订单，已接单", "订单，已接单，操作人【{0}/{1}】", 4),
    FINISH("已完成", "订单，已完成", "订单，已完成，操作人【{0}/{1}】", 100),
    CANCEL("已取消", "车辆，已取消", "车辆, 已取消，操作人{0}/{1}", 113);


    private String name;
    private String outterLog;
    private String innerLog;
    private int code;

    OrderLogEnum(String name, String outterLog, String innerLog, int code) {
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
    }
}
