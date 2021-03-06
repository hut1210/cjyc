package com.cjyc.common.model.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cjyc.common.model.serizlizer.BigDecimalSerizlizer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 订单明细（车辆表）
 * </p>
 *
 * @author JPG
 * @since 2019-11-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("w_order_car")
@ApiModel(value="OrderCar对象", description="订单明细（车辆表）")
public class OrderCar implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "订单ID")
    private Long orderId;

    @ApiModelProperty(value = "订单编号")
    @Excel(name = "订单编号", orderNum = "20",width = 15)
    private String orderNo;

    @ApiModelProperty(value = "车辆编码")
    @Excel(name = "车辆编码", orderNum = "5",width = 15)
    private String no;

    @ApiModelProperty(value = "品牌")
    @Excel(name = "品牌", orderNum = "9",width = 15)
    private String brand;

    @ApiModelProperty(value = "型号")
    @Excel(name = "车系", orderNum = "10",width = 15)
    private String model;

    @ApiModelProperty(value = "车牌号")
    @Excel(name = "车牌号", orderNum = "11",width = 15)
    private String plateNo;

    @ApiModelProperty(value = "vin码")
    @Excel(name = "vin码", orderNum = "6",width = 15)
    private String vin;

    @ApiModelProperty(value = "是否能动 0-否 1-是")
    private Integer isMove;

    @ApiModelProperty(value = "是否新车 0-否 1-是")
    private Integer isNew;

    @ApiModelProperty(value = "估值/万")
    @Excel(name = "车值(万元)", orderNum = "12", type = 10)
    private Integer valuation;

    @ApiModelProperty(value = "当前所在地所属业务中心")
    private Long nowStoreId;

    @ApiModelProperty(value = "当前所在区")
    private String nowAreaCode;

    @ApiModelProperty(value = "当前位置更新时间")
    private Long nowUpdateTime;

    @ApiModelProperty(value = "状态：0待路由，5待提车调度，10待提车，12待自送交车，15提车中（待交车），25待干线调度<循环>（提车入库），35待干线提车<循环>，40干线中<循环>（待干线交车），45待配送调度（干线入库），50待配送提车，55配送中（待配送交车），70待自取提车，100已签收")
    private Integer state;

//    @Excel(name = "订单状态", orderNum = "9")
    @TableField(exist = false)
    private String stateStr;

    @ApiModelProperty(value = "提车状态(调度状态)：1待调度，5已调度，7无需调度")
    private Integer pickState;

    @ApiModelProperty(value = "干线状态(调度状态)：1待调度，2节点调度，5已调度，7无需调度")
    private Integer trunkState;

    @ApiModelProperty(value = "送车状态(调度状态)：1待调度，5已调度，7无需调度")
    private Integer backState;

    @ApiModelProperty(value = "车辆描述")
    private String description;

    @ApiModelProperty(value = "车辆应收提车费")
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal pickFee;

    @ApiModelProperty(value = "车辆应收干线费")
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal trunkFee;

    @ApiModelProperty(value = "车辆应收送车费")
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal backFee;

    @ApiModelProperty(value = "实际提车类型")
    private Integer pickType;

    @ApiModelProperty(value = "实际送车类型")
    private Integer backType;


    @ApiModelProperty(value = "车辆应收保险费")
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal addInsuranceFee;

    @ApiModelProperty(value = "保额/万")
    @Excel(name = "追保额(万元)", orderNum = "13", type = 10)
    private Integer addInsuranceAmount;

    @ApiModelProperty(value = "物流券抵消金额")
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal couponOffsetFee;

    @ApiModelProperty(value = "车辆代收中介费（为资源合伙人代收）")
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal agencyFee;

    @ApiModelProperty(value = "单车总费用")
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal totalFee;

    @ApiModelProperty(value = "应收状态：0未支付，2已支付")
    private Integer wlPayState;

    @ApiModelProperty(value = "物流费支付时间")
    private Long wlPayTime;

    @ApiModelProperty(value = "完成时间")
    private Long finishTime;

    @ApiModelProperty(value = "允许放车标识：-1无限制，0未付款不允许放车，1未付款允许放车，2已付款不允许放车，9已付款允许放车")
    private Integer releaseCarFlag;

    @ApiModelProperty(value = "允许放车标识更新时间")
    private Long releaseCarFlagTime;


}
