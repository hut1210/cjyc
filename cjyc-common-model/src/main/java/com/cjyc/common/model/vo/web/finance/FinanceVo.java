package com.cjyc.common.model.vo.web.finance;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Author:Hut
 * @Date:2019/11/19 15:28
 */
@Data
public class FinanceVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "车辆编号")
    private String no;
    @ApiModelProperty(value = "品牌")
    private String brand;
    @ApiModelProperty(value = "型号")
    private String model;
    @ApiModelProperty(value = "vin码")
    private String vin;
    @ApiModelProperty(value = "订单编号")
    private String orderNo;
    @ApiModelProperty(value = "所属大区")
    private String largeArea;
    @ApiModelProperty(value = "订单所属业务中心")
    private String inputStoreName;
    @ApiModelProperty(value = "订单始发地")
    private String startAddress;
    @ApiModelProperty(value = "订单目的地")
    private String endAddress;
    @ApiModelProperty(value = "交付日期")
    private String deliveryDate;

    @ApiModelProperty(value = "客户Id")
    private Long customerId;
    @ApiModelProperty(value = "客户类型：1个人，2企业，3合伙人 ")
    private Integer type;

    private String customTypeName;

    @ApiModelProperty(value = "客户名称")
    private String customerName;
    @ApiModelProperty(value = "结算类型(0：时付  1：账期)")
    private Integer  payMode;

    private String payModeName;

    @ApiModelProperty(value = "订单分摊金额")
    private BigDecimal feeShare;
    @ApiModelProperty(value = "应收运费")
    private BigDecimal freightReceivable;
    @ApiModelProperty(value = "开票金额")
    private BigDecimal invoiceFee;
    @ApiModelProperty(value = "差额")
    private BigDecimal Difference;
    @ApiModelProperty(value = "实收金额")
    private BigDecimal amountReceived;
    @ApiModelProperty(value = "收款时间")
    private String receivedTime;

    @ApiModelProperty(value = "收入合计")
    private BigDecimal totalIncome;
    @ApiModelProperty(value = "成本合计")
    private BigDecimal totalCost;

    @ApiModelProperty(value = "毛利")
    private BigDecimal GrossProfit;

    @ApiModelProperty(value = "提车成本")
    private BigDecimal pickUpCarFee;

    @ApiModelProperty(value = "干线成本")
    private BigDecimal trunkLineFee;

    @ApiModelProperty(value = "送车成本")
    private BigDecimal carryCarFee;

    @ApiModelProperty(value = "提车成本列表")
    private List<TrunkLineVo> pickUpCarList;

    @ApiModelProperty(value = "干线成本列表")
    private List<TrunkLineVo> trunkLineVoList;

    @ApiModelProperty(value = "送车成本列表")
    private List<TrunkLineVo> carryCarList;
}
