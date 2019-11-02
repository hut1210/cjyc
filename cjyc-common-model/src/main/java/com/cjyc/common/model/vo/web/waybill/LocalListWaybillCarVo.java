package com.cjyc.common.model.vo.web.waybill;

import com.cjyc.common.model.entity.WaybillCar;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LocalListWaybillCarVo extends WaybillCar {

    @ApiModelProperty(value = "承运商ID")
    private Long carrierId;
    @ApiModelProperty(value = "承运商类型：0承运商，1业务员，2客户自己")
    private Integer carrierType;
    @ApiModelProperty(value = "承运商名称")
    private String carrierName;
    @ApiModelProperty(value = "调度人")
    private String createUser;
    @ApiModelProperty(value = "调度人ID")
    private Long createUserId;


    @ApiModelProperty(value = "品牌")
    private String brand;
    @ApiModelProperty(value = "型号")
    private String model;
    @ApiModelProperty(value = "车牌号")
    private String plateNo;
    @ApiModelProperty(value = "vin码")
    private String vin;


    @ApiModelProperty(value = "司机名称")
    private String driverName;
    @ApiModelProperty(value = "司机电话")
    private String driverPhone;
    @ApiModelProperty(value = "司机ID(userId)")
    private Long driverId;
    @ApiModelProperty(value = "运力车牌号")
    private String vehiclePlateNo;
}
