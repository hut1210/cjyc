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
 * 业务员推广明细
 * </p>
 *
 * @author JPG
 * @since 2019-11-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_admin_promote_detail")
@ApiModel(value="AdminPromoteDetail对象", description="业务员推广明细")
public class AdminPromoteDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "分享业务员ID")
    private Long promoteUserId;

    @ApiModelProperty(value = "注册用户ID")
    private Long customerId;

    @ApiModelProperty(value = "注册用户名称")
    private String customerName;

    @ApiModelProperty(value = "状态：0用户未下单，1用户已下单")
    private Integer state;

    @ApiModelProperty(value = "总收益")
    private BigDecimal incomeAmount;

    @ApiModelProperty(value = "已结算收益")
    private BigDecimal settlingAmount;

    @ApiModelProperty(value = "未结算收益")
    private BigDecimal unsettleAmount;

    @ApiModelProperty(value = "注册用户类型：2-司机；3-客户")
    private Integer type;

    @ApiModelProperty(value = "注册时间")
    private Long createTime;

    @ApiModelProperty(value = "修改时间")
    private Long updateTime;


}
