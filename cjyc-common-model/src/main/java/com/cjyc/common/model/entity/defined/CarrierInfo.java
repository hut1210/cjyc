package com.cjyc.common.model.entity.defined;

import lombok.Data;

@Data
public class CarrierInfo {
    private boolean isReAllotCarrier = true; //是否重新分配承运商
    private Long carrierId;
    private String carrierName;
    private Integer carrierType;

    private Long driverId;
    private String driverName;
    private String driverPhone;

    private Long vehicleId;
    private String vehiclePlateNo;

}
