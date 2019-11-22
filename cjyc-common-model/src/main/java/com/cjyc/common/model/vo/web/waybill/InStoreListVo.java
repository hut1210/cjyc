package com.cjyc.common.model.vo.web.waybill;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class InStoreListVo {


    @ApiModelProperty(value = "状态")
    private String outterState;

    @ApiModelProperty(value = "品牌")
    private String brand;

    @ApiModelProperty(value = "型号")
    private String model;

    @ApiModelProperty(value = "车牌号")
    private String plateNo;

    @ApiModelProperty(value = "vin码")
    private String vin;


    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "运单ID")
    private Long waybillId;

    @ApiModelProperty(value = "运单编号")
    private String waybillNo;

    @ApiModelProperty(value = "订单车辆ID")
    private Long orderCarId;

    @ApiModelProperty(value = "车辆编号")
    private String orderCarNo;

    @ApiModelProperty(value = "运费")
    private BigDecimal freightFee;

    @ApiModelProperty(value = "市")
    private String startCity;

    @ApiModelProperty(value = "市编码")
    private String startCityCode;

    @ApiModelProperty(value = "装车地址")
    private String startAddress;

    @ApiModelProperty(value = "出发地业务中心ID")
    private Long startStoreId;

    @ApiModelProperty(value = "起始地是否固定")
    private Boolean startFixedFlag;

    @ApiModelProperty(value = "市")
    private String endCity;

    @ApiModelProperty(value = "市编码")
    private String endCityCode;

    @ApiModelProperty(value = "目的地业务中心名称")
    private String endStoreName;

    @ApiModelProperty(value = "目的地业务中心ID")
    private Long endStoreId;

    @ApiModelProperty(value = "目的地是否固定")
    private Boolean endFixedFlag;

    @ApiModelProperty(value = "运单车辆状态：0待指派，2已指派，5待装车，15待装车确认，45已装车，70已卸车，90确认交车, 100确认收车, 105待重连，120已重连")
    private Integer state;

    @ApiModelProperty(value = "收车人名称")
    private String unloadLinkName;

    @ApiModelProperty(value = "收车联系人userId")
    private Long unloadLinkUserId;

    @ApiModelProperty(value = "收车人电话")
    private String unloadLinkPhone;

    @ApiModelProperty(value = "图片地址，逗号分隔")
    private String unloadPhotoImg;

    @ApiModelProperty(value = "实际完成卸车时间")
    private Long unloadTime;
}
