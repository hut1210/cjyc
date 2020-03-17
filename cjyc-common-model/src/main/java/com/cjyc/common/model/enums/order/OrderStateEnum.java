package com.cjyc.common.model.enums.order;

import com.cjyc.common.model.enums.UserTypeEnum;

/**
 * 订单状态枚举
 * @author JPG
 */
public enum OrderStateEnum {

    /**
     * 0待提交，2待分配，5待确认，10待复确认，15待预付款，25已确认，55运输中，88待付款，
     * 100已完成，111原返（待），112异常结束，113取消（待），114作废（待）
     */
    WAIT_SUBMIT("预订单", 0),
    SUBMITTED("待确认(客户提交)", 2),
    WAIT_CHECK("待确认(业务提交)", 5),
    //WAIT_RECHECK("待复确认", 10),
    WAIT_PREPAY("待支付",15),
    CHECKED("待调度",25),
    TRANSPORTING("运输中",55),
    FINISHED("已完成",100),
    //F_RETURN("原返（待）",111),
    //F_EXCEPTION("异常结束",112),
    F_CANCEL("已取消",113),
    F_OBSOLETE("已作废",114);

    public String name;
    public int code;

    OrderStateEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }

    public static OrderStateEnum valueOf(Integer code) {
        for (OrderStateEnum orderStateEnum : values()) {
            if(orderStateEnum.code == code){
                return orderStateEnum;
            }
        }
        throw new IllegalArgumentException("No matching constant for [" + code + "]");
    }}
