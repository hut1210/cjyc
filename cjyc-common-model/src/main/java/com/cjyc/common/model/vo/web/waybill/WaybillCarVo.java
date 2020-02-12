package com.cjyc.common.model.vo.web.waybill;

import com.cjyc.common.model.entity.WaybillCar;
import com.cjyc.common.model.serizlizer.BigDecimalSerizlizer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class WaybillCarVo extends WaybillCar {

    @ApiModelProperty(value = "状态")
    private String outterState;
    @ApiModelProperty(value = "历史照片")
    private String historyPhotoImg;
    @ApiModelProperty(value = "历史照片")
    private String logoImg;
    @ApiModelProperty(value = "品牌")
    private String brand;

    @ApiModelProperty(value = "型号")
    private String model;

    @ApiModelProperty(value = "车牌号")
    private String plateNo;

    @ApiModelProperty(value = "vin码")
    private String vin;
    @ApiModelProperty(value = "车辆状态")
    private Integer orderCarState;
    @ApiModelProperty(value = "价卡运费")
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal lineFreightFee;

    @ApiModelProperty(value = "始发地是否可以编辑")
    private Boolean startFixedFlag;

    @ApiModelProperty(value = "目的地是否可以编辑")
    private Boolean endFixedFlag;
    @ApiModelProperty(value = "目的地是否可以编辑")
    private Boolean hasLine;

    @ApiModelProperty(value = "提车方式：1自送，2代驾上门，3拖车上门，4物流上门")
    private String pickType;

    @ApiModelProperty(value = "送车方式：1自提，2代驾上门，3拖车上门，4物流上门")
    private String backType;

    @ApiModelProperty(value = "提车状态(调度状态)：1待调度，5已调度，7无需调度")
    private Integer pickState;

    @ApiModelProperty(value = "干线状态(调度状态)：1待调度，2节点调度，5已调度，7无需调度")
    private Integer trunkState;

    @ApiModelProperty(value = "送车状态(调度状态)：1待调度，5已调度，7无需调度")
    private Integer backState;


    private String orderStartCityCode;
    private String orderEndCityCode;
    private String startStoreFullAddress;
    private String endStoreFullAddress;

}
