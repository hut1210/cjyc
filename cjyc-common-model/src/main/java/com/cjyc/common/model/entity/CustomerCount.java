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
 * 客户订单车辆统计表
 * </p>
 *
 * @author JPG
 * @since 2019-10-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("c_customer_count")
@ApiModel(value="CustomerCount对象", description="客户订单车辆统计表")
public class CustomerCount implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "客户userId")
    private Long customerUserId;

    @ApiModelProperty(value = "下单量")
    private Integer orderNum;

    @ApiModelProperty(value = "总运量（台）")
    private Integer orderCarNum;

    @ApiModelProperty(value = "订单总金额（分）")
    private BigDecimal orderTotalCost;


}
