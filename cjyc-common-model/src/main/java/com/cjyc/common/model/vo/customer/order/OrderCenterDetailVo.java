package com.cjyc.common.model.vo.customer.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Description 订单明细实体类
 * @Author LiuXingXiang
 * @Date 2019/10/31 18:45
 **/
@Data
public class OrderCenterDetailVo implements Serializable {
    private static final long serialVersionUID = 2193129578098454937L;
    @ApiModelProperty(value = "订单编号")
    private String no;

    @ApiModelProperty(value = "订单状态：0待提交，2待分配，5待确认，10待复确认，15待预付款，25已确认，55运输中，" +
            "88待付款，100已完成，111原返（待），112异常结束，113取消（待），114作废（待）")
    private String state;

    @ApiModelProperty(value = "下单时间")
    private Long createTime;

    @ApiModelProperty(value = "托运车辆信息列表")
    private List<OrderCarCenterVo> orderCarCenterVoList;

    @ApiModelProperty(value = "托运车辆信息列表:运输中已交付车辆")
    private List<OrderCarCenterVo> orderCarFinishPayList;

    @ApiModelProperty(value = "预计出发时间（提车日期）")
    private Long expectStartDate;

    @ApiModelProperty(value = "发车人")
    private String pickContactName;

    @ApiModelProperty(value = "发车人联系方式")
    private String pickContactPhone;

    @ApiModelProperty(value = "提车方式:1 自送，2代驾上门，3拖车上门，4物流上门")
    private Integer pickType;

    @ApiModelProperty(value = "出发地详细地址")
    private String startAddress;

    @ApiModelProperty(value = "收车人")
    private String backContactName;

    @ApiModelProperty(value = "收车人联系方式")
    private String backContactPhone;

    @ApiModelProperty(value = "送车方式： 1 自提，2代驾上门，3拖车上门，4物流上门")
    private Integer backType;

    @ApiModelProperty(value = "目的地详细地址")
    private String endAddress;

    @ApiModelProperty(value = "应收提车费")
    private BigDecimal pickFee;

    @ApiModelProperty(value = "应收配送费")
    private BigDecimal backFee;

    @ApiModelProperty(value = "应收干线费")
    private BigDecimal trunkFee;

    @ApiModelProperty(value = "应收订单定金")
    private BigDecimal depositFee;

    @ApiModelProperty(value = "应收追加保险费")
    private BigDecimal addInsuranceFee;

    @ApiModelProperty(value = "应收总价：收车后客户应支付平台的费用，计算值")
    private BigDecimal totalFee;
}
