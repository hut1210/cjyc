package com.cjyc.common.model.dto.web.order;

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
 * 订单明细（车辆表）
 * </p>
 *
 * @author JPG
 * @since 2019-10-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel
public class OrderCarWaitDispatchListDto implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "调度类型")
    private Long dispatchType;

    @ApiModelProperty(value = "省编号")
    private String startProvinceCode;

    @ApiModelProperty(value = "市编号")
    private String startCityCode;

    @ApiModelProperty(value = "区编号")
    private String startAreaCode;

    @ApiModelProperty(value = "省编号")
    private String endProvinceCode;

    @ApiModelProperty(value = "市编号")
    private String endCityCode;

    @ApiModelProperty(value = "区编号")
    private String endAreaCode;

    @ApiModelProperty(value = "订单ID")
    private Long orderId;

    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "车辆编码")
    private String no;

    @ApiModelProperty(value = "vin码")
    private String vin;

    @ApiModelProperty(value = "预计出发时间（提车日期）")
    private Long expectStartDate;


    @ApiModelProperty(value = "提车方式:1 自送，2代驾上门，3拖车上门")
    private Integer pickType;

    @ApiModelProperty(value = "发车人")
    private String pickContactName;

    @ApiModelProperty(value = "发车人联系方式")
    private String pickContactPhone;

    @ApiModelProperty(value = "送车方式： 1 自提，2代驾上门，3拖车上门")
    private Integer backType;

    @ApiModelProperty(value = "收车人")
    private String backContactName;

    @ApiModelProperty(value = "收车人联系方式")
    private String backContactPhone;





    @ApiModelProperty(value = "品牌")
    private String brand;

    @ApiModelProperty(value = "型号")
    private String model;

    @ApiModelProperty(value = "车牌号")
    private String plateNo;


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

    @ApiModelProperty(value = "应收状态：0未支付，1已支付")
    private Integer wlPayState;

}
