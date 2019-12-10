package com.cjyc.common.model.vo.web.finance;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author: Hut
 * @Date: 2019/12/9 15:41
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
    @ApiModelProperty(value = "交付时间")
    private Long  paymentTime;
    @ApiModelProperty(value = "结算类型")
    private String  payModeStr;
    @ApiModelProperty(value = "应收运费")
    private BigDecimal freightReceivable;

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

    @ApiModelProperty(value = "交付日期")
    private String deliveryDate;
    @ApiModelProperty(value = "客户类型")
    private Integer type;

    private String customTypeName;
    @ApiModelProperty(value = "客户名称")
    private String customerName;

}
