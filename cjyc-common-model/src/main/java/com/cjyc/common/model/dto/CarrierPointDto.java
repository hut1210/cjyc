package com.cjyc.common.model.dto;

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
 * 承运商分表
 * </p>
 *
 * @author JPG
 * @since 2019-10-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("d_carrier_point")
@ApiModel(value="CarrierPointDto对象", description="承运商分表")
public class CarrierPointDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    @ApiModelProperty(value = "司机ID")
    private Long carrierId;

    @ApiModelProperty(value = "锁定积分")
    private Integer protectedPoint;

    @ApiModelProperty(value = "可用积分")
    private Integer availablePoint;

    @ApiModelProperty(value = "状态：")
    private Integer state;

    @ApiModelProperty(value = "总收入积分")
    private Integer totalIncomePoint;

    @ApiModelProperty(value = "总花费积分")
    private Integer totalExpendPoint;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "更新时间")
    private String updateTime;


}
