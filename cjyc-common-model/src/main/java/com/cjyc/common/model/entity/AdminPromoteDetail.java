package com.cjyc.common.model.entity;

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
 * 业务员推广明细
 * </p>
 *
 * @author JPG
 * @since 2019-10-24
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

    @ApiModelProperty(value = "业务员ID")
    private Long userId;

    @ApiModelProperty(value = "推广的用户ID")
    private Long customerId;

    @ApiModelProperty(value = "状态：0用户未下单，1用户已下单")
    private Integer state;

    @ApiModelProperty(value = "状态：0未结算，1结算中，2已结算")
    private Integer settleState;

    @ApiModelProperty(value = "结算申请单号")
    private String settleBillNo;


}
