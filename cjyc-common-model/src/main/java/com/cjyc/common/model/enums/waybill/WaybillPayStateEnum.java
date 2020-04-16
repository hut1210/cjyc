package com.cjyc.common.model.enums.waybill;

/**
 * @Author: Hut
 * @Date: 2020/04/16 9:08
 *
 * 运单付款状态
 **/
public enum WaybillPayStateEnum {

    UNPAID("未付款",0),
    INPAYMENT("付款中",2),
    PAID("已付款",1),
    FAILED("付款失败",-2);

    public String name;
    public int code;

    WaybillPayStateEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
}
