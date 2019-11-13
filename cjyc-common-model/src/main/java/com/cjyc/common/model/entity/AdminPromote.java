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
 * 业务员推广表
 * </p>
 *
 * @author JPG
 * @since 2019-11-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_admin_promote")
@ApiModel(value="AdminPromote对象", description="业务员推广表")
public class AdminPromote implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "分享渠道")
    private String channel;

    @ApiModelProperty(value = "分享人ID")
    private Long userId;

    @ApiModelProperty(value = "分享人名称")
    private String userName;

    @ApiModelProperty(value = "有效推广人数")
    private Integer customerNumValid;

    @ApiModelProperty(value = "总收益")
    private BigDecimal incomeAmount;

    @ApiModelProperty(value = "结算中收益")
    private BigDecimal settlingAmount;

    @ApiModelProperty(value = "未结算收益")
    private BigDecimal unsettleAmount;

    @ApiModelProperty(value = "分享时间")
    private Long createTime;

    @ApiModelProperty(value = "修改时间")
    private Long updateTime;


}
