package com.cjyc.common.model.vo.web;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author: Hut
 * @Date: 2020/04/20 9:29
 *
 * 结算明细
 **/
@Data
public class SettlementDetailsVo implements Serializable {
    private static final long serialVersionUID = 1152187218002996582L;

    @ApiModelProperty(value = "运单单号")
    private String waybillNo;

    @ApiModelProperty(value = "车辆数")
    private int carNum;

    @ApiModelProperty(value = "交付日期")
    private Long deliveryDate;

    @ApiModelProperty(value = "交付日期")
    private String deliveryDateStr;

    @ApiModelProperty(value = "结算类型")
    private Integer settleType;

    @ApiModelProperty(value = "付款时间")
    private Long payTime;

    @ApiModelProperty(value = "付款时间")
    private String payTimeStr;

    @ApiModelProperty(value = "付款状态")
    private Integer payState;

    @ApiModelProperty(value = "付款状态")
    private String payStateStr;

    @ApiModelProperty(value = "应付运费（元）")
    private BigDecimal freightFee;

    @ApiModelProperty(value = "运单类型 1提车运单，2干线运单，3送车运单")
    private Integer waybillType;

    @ApiModelProperty(value = "运输路线")
    private String transPortLine;

    @ApiModelProperty(value = "承运商")
    private String carrierName;

    @ApiModelProperty(value = "司机")
    private String driverName;

    @ApiModelProperty(value = "司机电话")
    private String driverPhone;

    @ApiModelProperty(value = "车牌号")
    private String vehiclePlateNo;
}
