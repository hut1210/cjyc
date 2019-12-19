package com.cjyc.common.model.vo.web.waybill;

import com.cjyc.common.model.entity.WaybillCar;
import lombok.Data;

@Data
public class WaybillCarTransportVo extends WaybillCar {

    private String outterState;
    private Long carrierId;
    private Integer carrierType;
    private String carrierName;
    private String createUser;
    private Long createUserId;
    private String driverName;
    private String driverPhone;
    private Long driverId;
    private String vehiclePlateNo;

}
