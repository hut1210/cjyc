package com.cjyc.common.model.vo.web.mineCarrier;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author: Hut
 * @Date: 2019/12/18 15:59
 */
@Data
public class SettlementDetailVo implements Serializable {

    private static final long serialVersionUID = 1152187218002996582L;

    @ApiModelProperty("运单单号")
    private String wayBillNo;

    @ApiModelProperty("车辆数")
    private int carCount;

    @ApiModelProperty("交付日期")
    private Long completeTime;

    @ApiModelProperty("结算类型")
    private int payMode;

    @ApiModelProperty("付款时间")
    private Long payTime;

    @ApiModelProperty("付款状态")
    private String state;

    @ApiModelProperty("应付运费")
    private BigDecimal freightFee;

    @ApiModelProperty("运单类型")
    private int wayBillType;

    @ApiModelProperty("运输路线")
    private String transportLine;

    @ApiModelProperty("承运商")
    private String carrierName;

    @ApiModelProperty("司机名称")
    private String  driverName;

    @ApiModelProperty("司机电话")
    private String driverPhone;

    @ApiModelProperty("车牌号")
    private String plateNo;
}
