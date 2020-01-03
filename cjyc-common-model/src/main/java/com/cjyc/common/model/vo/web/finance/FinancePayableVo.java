package com.cjyc.common.model.vo.web.finance;

import com.cjyc.common.model.util.BigDecimalSerizlizer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author: Hut
 * @Date: 2020/01/03 11:03
 **/
@Data
public class FinancePayableVo implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "运单单号")
    private String no;

    @ApiModelProperty(value = "交付日期")
    private Long completeTime;

    @ApiModelProperty(value = "结算类型")
    private Integer type;

    @ApiModelProperty(value = "结算类型名称")
    private String settleTypeName;

    @ApiModelProperty(value = "账期时间")
    private int settlePeriod;

    @ApiModelProperty(value = "剩余账期时间")
    private Long remainDate;

    @ApiModelProperty(value = "应付运费")
    private BigDecimal freightPayable;

    @ApiModelProperty(value = "运单类型")
    private Integer waybillType;

    @ApiModelProperty(value = "运输路线")
    private String transportLine;

    @ApiModelProperty(value = "承运商ID")
    private Long carrierId;

    @ApiModelProperty(value = "承运类型")
    private Integer carrierType;

    @ApiModelProperty(value = "承运商名称")
    private String carrierName;

    @ApiModelProperty(value = "司机名称")
    private String driverName;

    @ApiModelProperty(value = "司机电话")
    private String driverPhone;

    @ApiModelProperty(value = "车牌号")
    private String vehiclePlateNo;
}
