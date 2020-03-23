package com.cjyc.common.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 应收账款结算详情表
 * </p>
 *
 * @author RenPL
 * @since 2020-3-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("s_settlement_detail")
@ApiModel(value = "ReceiveSettlementDetail", description = "应收账款结算详情表")
public class ReceiveSettlementDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "订单车辆id")
    private Long orderCarId;

    @ApiModelProperty(value = "结算流水号")
    private String serialNumber;

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
    private Long deliveryDate;

}
