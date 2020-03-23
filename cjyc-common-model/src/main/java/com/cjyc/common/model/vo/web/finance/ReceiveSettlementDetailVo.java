package com.cjyc.common.model.vo.web.finance;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
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
public class ReceiveSettlementDetailVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id", hidden = true)
    private Long id;

    @ApiModelProperty(value = "订单车辆id", required = true)
    private Long orderCarId;

    @ApiModelProperty(value = "结算流水号", hidden = true)
    private String serialNumber;

    @ApiModelProperty(value = "客户名称", required = true)
    private String customerName;

    @ApiModelProperty(value = "车辆编码", required = true)
    private String no;

    @ApiModelProperty(value = "vin码", required = true)
    private String vin;

    @ApiModelProperty(value = "品牌", required = true)
    private String brand;

    @ApiModelProperty(value = "车系", required = true)
    private String model;

    @ApiModelProperty(value = "应收运费", required = true)
    private BigDecimal freightReceivable;

    @ApiModelProperty(value = "开票金额", required = true)
    private BigDecimal invoiceFee;

    @ApiModelProperty(value = "始发地", required = true)
    private String startAddress;

    @ApiModelProperty(value = "目的地", required = true)
    private String endAddress;

    @ApiModelProperty(value = "交付日期", required = true)
    private Long deliveryDate;

}
