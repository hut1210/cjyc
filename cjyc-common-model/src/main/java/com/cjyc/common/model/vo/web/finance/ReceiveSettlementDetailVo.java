package com.cjyc.common.model.vo.web.finance;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>
 * 应收账款结算详情参数Vo
 * </p>
 *
 * @author RenPL
 * @since 2020-3-20
 */
@Data
@ApiModel(value = "ReceiveSettlementDetailVo", description = "应收账款结算详情参数Vo")
public class ReceiveSettlementDetailVo {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "订单车辆id")
    private Long orderCarId;

    @ApiModelProperty(value = "结算流水号")
    private String serialNumber;

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "车辆编码")
    private String no;

    @ApiModelProperty(value = "vin码")
    private String vin;

    @ApiModelProperty(value = "品牌")
    private String brand;

    @ApiModelProperty(value = "车系")
    private String model;

    @ApiModelProperty(value = "应收运费")
    private BigDecimal freightReceivable;

    @ApiModelProperty(value = "开票金额")
    private BigDecimal invoiceFee;

    @ApiModelProperty(value = "始发地")
    private String startAddress;

    @ApiModelProperty(value = "目的地")
    private String endAddress;

    @ApiModelProperty(value = "交付日期")
    private String deliveryDate;

}
