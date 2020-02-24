package com.cjyc.common.model.entity.defined;

import com.cjyc.common.model.enums.message.PushMsgEnum;
import lombok.Data;

import java.util.List;

@Data
public class PushCustomerInfo {
    private CarrierInfo carrierInfo;
    private PushMsgEnum pushMsgEnum;
    private List<String> orderCarNos;

    public PushCustomerInfo(CarrierInfo carrierInfo, PushMsgEnum pushMsgEnum, List<String> orderCarNos) {
        this.carrierInfo = carrierInfo;
        this.pushMsgEnum = pushMsgEnum;
        this.orderCarNos = orderCarNos;
    }
}
