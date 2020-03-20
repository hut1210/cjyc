package com.cjyc.common.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
@TableName("r_settlement_detail")
@ApiModel(value = "ReceiveSettlementDetail", description = "应收账款结算详情表")
public class ReceiveSettlementDetail {

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "结算流水号")
    private String serialNumber;

    @ApiModelProperty(value = "订单车辆id")
    private Long orderCarId;

}
