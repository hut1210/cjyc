package com.cjyc.common.model.enums.log;

public enum OrderLogEnum {

    /***/
    SUBMIT("订单提交-客户", "订单已提交，等待业务员确认", "订单已提交，等待业务员确认", 1),
    COMMIT("订单提交-业务员", "", "", 2),
    CHECK("订单审核", "订单{0}，已审核", "订单{0}，已审核", 3),

    WAIT_PREPAY("待付款", "订单确认完成，请支付运费", "订单确认完成，待预付物流费", 4),
    RECEIPT("签收", "订单{0}, 已全部签收", "订单{0}, 已所有车辆全部签收", 5)/*,
    CHECK("已确认",25),
    TRANSPORTING("运输中",55),
    WAIT_PAY("待付款",88),
    FINISHED("已完成",100),
    F_RETURN("原返（待）",111),
    F_EXCEPTION("异常结束",112),
    F_CANCEL("取消（待）",113),
    F_OBSOLETE("作废（待）",114)*/;


    private String name;
    private String innerLog;
    private String outterLog;
    private int code;

    OrderLogEnum(String name, String innerLog, String outterLog, int code) {
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
    }
}
