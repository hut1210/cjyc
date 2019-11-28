package com.cjyc.common.model.vo.web.finance;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author:Hut
 * @Date:2019/11/22 13:30
 */
@Data
public class FinanceReceiptVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "车辆编号")
    private String no;
    @ApiModelProperty(value = "品牌")
    private String brand;
    @ApiModelProperty(value = "型号")
    private String model;
    @ApiModelProperty(value = "vin码")
    private String vin;
    @ApiModelProperty(value = "结算类型")
    private Integer  payMode;

    private String payModeName;
    @ApiModelProperty(value = "剩余账期")
    private int remindDay;
    @ApiModelProperty(value = "应收运费")
    private BigDecimal freightreceivable;
    @ApiModelProperty(value = "订单编号")
    private String orderNo;
    @ApiModelProperty(value = "订单所属大区")
    private String largeArea;
    @ApiModelProperty(value = "订单所属业务中心")
    private String inputStoreName;
    @ApiModelProperty(value = "始发省")
    private String startProvince;
    @ApiModelProperty(value = "始发市")
    private String startCity;
    @ApiModelProperty(value = "始发区")
    private String startArea;

    @ApiModelProperty(value = "目的省")
    private String endProvince;
    @ApiModelProperty(value = "目的市")
    private String endCity;
    @ApiModelProperty(value = "目的区")
    private String endArea;

    @ApiModelProperty(value = "交付日期")
    private String deliveryDate;
    @ApiModelProperty(value = "客户类型")
    private Integer type;

    private String customTypeName;
    @ApiModelProperty(value = "客户名称")
    private String customerName;
}
