package com.cjyc.common.model.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 优惠券主表
 * </p>
 *
 * @author JPG
 * @since 2019-10-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("c_coupon_send")
@ApiModel(value="CouponSend对象", description="优惠券主表")
public class CouponSend implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "优惠券id")
    private Long couponId;

    @ApiModelProperty(value = "客户ID")
    private Long customerId;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "优惠券编码")
    private String couponNo;

    @ApiModelProperty(value = "是否已使用 0：领取未使用  1：已使用")
    private Integer isUse;

    @ApiModelProperty(value = "优惠券领取时间")
    private Long receiveTime;

    @ApiModelProperty(value = "优惠券使用时间")
    private Long useTime;

    @ApiModelProperty(value = "折扣金额")
    private BigDecimal discountAmount;

    @ApiModelProperty(value = "最终订单金额")
    private BigDecimal finalOrderAmount;


}
