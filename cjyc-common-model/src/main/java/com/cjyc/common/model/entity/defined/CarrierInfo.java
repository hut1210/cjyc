package com.cjyc.common.model.entity.defined;

import com.cjyc.common.model.enums.transport.CarrierTypeEnum;
import lombok.Data;

@Data
public class CarrierInfo {
    private boolean isReAllotCarrier = true; //是否重新分配承运商
    private Long carrierId;
    private String carrierName;
    private Integer carrierType = CarrierTypeEnum.PERSONAL.code;//承运商类型 1个人，2企业
    private Integer carrierPhone;

    private Integer carryType;//承运方式

    private Long driverId;
    private String driverName;
    private String driverPhone;

    private Long vehicleId;
    private String vehiclePlateNo;

}
