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
 * 任务表(子运单)
 * </p>
 *
 * @author JPG
 * @since 2019-11-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("w_task")
@ApiModel(value="Task对象", description="任务表(子运单)")
public class Task implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "任务编号")
    private String no;

    @ApiModelProperty(value = "运单ID")
    private Long waybillId;

    @ApiModelProperty(value = "运单编号")
    private String waybillNo;

    @ApiModelProperty(value = "车辆数")
    private Integer carNum;

    @ApiModelProperty(value = "任务状态：0待承接，5待装车，55运输中，100已完成，113已取消，115已拒接")
    private Integer state;

    @ApiModelProperty(value = "司机名称")
    private String driverName;

    @ApiModelProperty(value = "司机电话")
    private String driverPhone;

    @ApiModelProperty(value = "司机ID(userId)")
    private Long driverId;

    @ApiModelProperty(value = "装车数量")
    private Integer loadCarNum;

    @ApiModelProperty(value = "卸车数量")
    private Integer unloadCarNum;

    @ApiModelProperty(value = "实时运力ID")
    private Long vehicleRunningId;

    @ApiModelProperty(value = "运力车牌号")
    private String vehiclePlateNo;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建人名称")
    private String createUser;

    @ApiModelProperty(value = "创建人ID")
    private Long createUserId;

    @ApiModelProperty(value = "创建时间")
    private Long createTime;


}
