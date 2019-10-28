package com.cjyc.common.model.dto.web.order;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel
public class ChangePriceOrderCarDto {

    @ApiModelProperty(value = "ID")
    private Long id;
    @ApiModelProperty(value = "no")
    private Long no;
    @ApiModelProperty(value = "vin")
    private Long vin;

    @ApiModelProperty(value = "车辆应收提车费")
    private BigDecimal pickFee;

    @ApiModelProperty(value = "车辆应收干线费")
    private BigDecimal trunkFee;

    @ApiModelProperty(value = "车辆应收送车费")
    private BigDecimal backFee;

    @ApiModelProperty(value = "车辆应收保险费")
    private BigDecimal insuranceFee;

    @ApiModelProperty(value = "物流券抵消金额")
    private BigDecimal couponOffsetFee;

    @ApiModelProperty(value = "车辆代收中介费（为资源合伙人代收）")
    private BigDecimal agencyFee;



}
