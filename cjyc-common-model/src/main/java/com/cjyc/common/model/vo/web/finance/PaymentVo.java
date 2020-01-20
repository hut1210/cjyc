package com.cjyc.common.model.vo.web.finance;

import com.cjyc.common.model.serizlizer.BigDecimalSerizlizer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author: Hut
 * @Date: 2019/12/9 15:
 * 已收款（时付）
 */
@Data
public class PaymentVo implements Serializable {

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
    private String  payModeName;

    @JsonSerialize(using = BigDecimalSerizlizer.class)
    @ApiModelProperty(value = "应收运费")
    private BigDecimal freightReceivable;

    @JsonSerialize(using = BigDecimalSerizlizer.class)
    @ApiModelProperty(value = "实付运费")
    private BigDecimal freightPay;
    @ApiModelProperty(value = "订单编号")
    private String orderNo;
    @ApiModelProperty(value = "订单所属大区")
    private String largeArea;
    @ApiModelProperty(value = "订单所属业务中心")
    private String inputStoreName;
    @ApiModelProperty(value = "始发地")
    private String startAddress;

    @ApiModelProperty(value = "目的地")
    private String endAddress;

    @ApiModelProperty(value = "交付时间")
    private Long deliveryDate;

    @ApiModelProperty(value = "客户Id")
    private Long customerId;
    @ApiModelProperty(value = "客户类型")
    private Integer type;

    private String customTypeName;
    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "合同Id")
    private Long customerContractId;
}
