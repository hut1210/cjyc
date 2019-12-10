package com.cjyc.common.model.enums.log;

/**
 * 轨迹日志枚举
 * @author JPG
 */
public enum OrderCarLogEnum {

    /***/
    SUBMIT("客户提交日志", "", "", 1),
    COMMIT("业务员提交日志", "", "", 2),
    CHECK("客户提交日志", "", "", 3),
    IN_STORE("客户提交日志", "", "", 3),
    RECEIPT("客户签收日志", "车辆{0}, 已被签收", "车辆{0}, 已被签收", 4);

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
