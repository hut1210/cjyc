package com.cjyc.foreign.api.dto.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Description 订单车辆信息
 * @Author Liu Xing Xiang
 * @Date 2020/3/12 11:40
 **/
@Data
public class OrderCarSaveDto implements Serializable {
    private static final long serialVersionUID = -3441938847460577267L;
    @ApiModelProperty(value = "品牌")
    private String brand;

    @ApiModelProperty(value = "型号")
    private String model;

    @Pattern(regexp = "(^$)|(^\\S{1,20}$)", message = "车牌号格式不正确，请检查")
    @ApiModelProperty(value = "车牌号")
    private String plateNo;

    @Pattern(regexp = "(^$)|(^[0-9a-zA-Z]{1,20}$)", message = "vin码格式不正确，请检查")
    @ApiModelProperty(value = "vin码")
    private String vin;

    @ApiModelProperty(value = "是否能动 0-否 1-是")
    private int isMove;

    @ApiModelProperty(value = "是否新车 0-否 1-是")
    private int isNew;

    @ApiModelProperty(value = "估值/分")
    private int valuation;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "车辆应收保险费")
    private BigDecimal addInsuranceFee;

    @ApiModelProperty(value = "保额/万")
    private Integer addInsuranceAmount;

    @ApiModelProperty(value = "车辆应收提车费 单位：分")
    private BigDecimal pickFee;

    @ApiModelProperty(value = "车辆应收干线费 单位：分")
    private BigDecimal trunkFee;

    @ApiModelProperty(value = "车辆应收送车费 单位：分")
    private BigDecimal backFee;

    @ApiModelProperty(value = "物流券抵消金额")
    private BigDecimal couponOffsetFee;
}
