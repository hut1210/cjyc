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
 * 任务明细表(车辆表)
 * </p>
 *
 * @author JPG
 * @since 2019-10-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("w_task_car")
@ApiModel(value="TaskCar对象", description="任务明细表(车辆表)")
public class TaskCar implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "任务ID")
    private Long taskId;

    @ApiModelProperty(value = "运单ID")
    private Long waybillId;

    @ApiModelProperty(value = "运单车辆ID")
    private Long waybillCarId;

    @ApiModelProperty(value = "订单车辆ID")
    private String orderCarId;

    @ApiModelProperty(value = "任务车辆状态：1待装车，2已装车，4已卸车，9确认收车")
    private Integer state;

    @ApiModelProperty(value = "图片地址，逗号分隔")
    private String photoImg;

    @ApiModelProperty(value = "装车时间")
    private Long loadTime;

    @ApiModelProperty(value = "卸车时间")
    private Long unloadTime;

    @ApiModelProperty(value = "车辆运费")
    private BigDecimal freightFee;

    @ApiModelProperty(value = "创建时间")
    private Long createTime;


}
