package com.cjyc.common.model.enums.log;

/**
 * 轨迹日志枚举
 * @author JPG
 */
public enum OrderCarLogEnum {

    /***/
    SUBMIT("客户提交日志", "", ""),
    COMMIT("业务员提交日志", "", ""),
    CHECK("客户提交日志", "", ""),
    RECEPIT("客户签收日志", "车辆{0}, 已被签收", "车辆{0}, 已被签收");

    private String name;
    private String innerLog;
    private String outterLog;

    OrderCarLogEnum(String name, String innerLog, String outterLog) {
        this.name = name;
        this.innerLog = innerLog;
        this.outterLog = outterLog;
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

}
