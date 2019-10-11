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
 * 业务员推广表
 * </p>
 *
 * @author JPG
 * @since 2019-10-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_saleman_promote")
@ApiModel(value="SalemanPromote对象", description="业务员推广表")
public class SalemanPromote implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "user_id")
    private Long userId;

    @ApiModelProperty(value = "推广人数")
    private Integer customerNum;

    @ApiModelProperty(value = "有效推广人数")
    private Integer customerNumValid;

    @ApiModelProperty(value = "总收益")
    private BigDecimal incomeAmount;

    @ApiModelProperty(value = "结算中收益")
    private BigDecimal settlingAmount;

    @ApiModelProperty(value = "未结算收益")
    private BigDecimal unsettleAmount;


}
