package com.cjyc.common.model.vo.web.finance;

import com.cjyc.common.model.serizlizer.BigDecimalSerizlizer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author: Hut
 * @Date: 2019/12/11 10:08
 */
@Data
public class PaidNewVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "运单单号")
    private String waybillNo;

    @ApiModelProperty(value = "交付日期")
    private Long completeTime;

    @ApiModelProperty(value = "结算类型")
    private String settleType;

    @JsonSerialize(using = BigDecimalSerizlizer.class)
    @ApiModelProperty(value = "应付运费")
    private BigDecimal freightFee;

    @ApiModelProperty(value = "运单类型")
    private Integer type;

    @ApiModelProperty(value = "运输线路")
    private String guideLine;

    @ApiModelProperty(value = "承运商")
    private String carrierName;

    @ApiModelProperty(value = "司机名称")
    private String driverName;

    @ApiModelProperty(value = "司机电话")
    private String driverPhone;

    @ApiModelProperty(value = "车牌号")
    private String vehiclePlateNo;

    @ApiModelProperty(value = "付款状态 0未付款 2已付款")
    private String state;

    @ApiModelProperty(value = "付款操作人")
    private String operator;
}
