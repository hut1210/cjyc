package com.cjyc.common.model.vo.web;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 通用OrderCarVo，附加订单信息（不用做返回信息）
 * </p>
 *
 * @author JPG
 * @since 2019-10-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel
public class OrderCarVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long orderCarId;

    @ApiModelProperty(value = "订单ID")
    private Long orderId;

    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "车辆编码")
    private String orderCarno;

    @ApiModelProperty(value = "品牌")
    private String brand;

    @ApiModelProperty(value = "型号")
    private String model;

    @ApiModelProperty(value = "车牌号")
    private String plateNo;

    @ApiModelProperty(value = "vin码")
    private String vin;

    @ApiModelProperty(value = "是否能动 0-否 1-是")
    private int isMove;

    @ApiModelProperty(value = "是否新车 0-否 1-是")
    private int isNew;

    @ApiModelProperty(value = "估值/万")
    private Integer valuation;

    @ApiModelProperty(value = "当前所在地所属业务中心")
    private Long nowStoreId;

    @ApiModelProperty(value = "当前所在区")
    private String nowAreaCode;

    @ApiModelProperty(value = "状态：0待路由，"
            + "5待提车调度，"
            + "10待提车，"
            + "15提车中（待交车），"
            + "20待自送交车"
            + "25待干线调度<循环>（提车入库），"
            + "35待干线提车<循环>，"
            + "40干线中<循环>（待干线交车），"
            + "45待配送调度（干线入库），"
            + "50待配送提车，"
            + "55配送中（待配送交车），"
            + "70待自取提车，"
            + "100已签收")
    private Integer state;

    @ApiModelProperty(value = "车辆描述")
    private String description;

    @ApiModelProperty(value = "车辆应收提车费")
    private BigDecimal pickFee;

    @ApiModelProperty(value = "车辆应收干线费")
    private BigDecimal trunkFee;

    @ApiModelProperty(value = "车辆应收送车费")
    private BigDecimal backFee;

    @ApiModelProperty(value = "车辆应收保险费")
    private BigDecimal insuranceFee;

    @ApiModelProperty(value = "保额/万")
    private Integer insuranceCoverageAmount;

    @ApiModelProperty(value = "车辆代收中介费（为资源合伙人代收）")
    private BigDecimal agencyFee;

    @ApiModelProperty(value = "单车总费用")
    private BigDecimal totalFee;

    @ApiModelProperty(value = "应收状态：0未支付，1已支付")
    private Integer wlPayState;

}
