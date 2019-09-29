package com.cjyc.common.model.entity.auto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 任务表(子运单)
 * </p>
 *
 * @author JPG
 * @since 2019-09-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("w_task")
public class Task implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    /**
     * 任务编号
     */
    @TableField("no")
    private String no;

    /**
     * 运单ID
     */
    @TableField("waybill_id")
    private Long waybillId;

    /**
     * 车辆数
     */
    @TableField("car_num")
    private Integer carNum;

    /**
     * 任务状态：0待承接，5待装车，10运输中，90部分完成，100已完成，102已取消，103已拒接
     */
    @TableField("state")
    private Integer state;

    /**
     * 司机名称
     */
    @TableField("driver_name")
    private Integer driverName;

    /**
     * 司机ID
     */
    @TableField("driver_id")
    private Long driverId;

    /**
     * 装车数量
     */
    @TableField("load_car_num")
    private Integer loadCarNum;

    /**
     * 卸车数量
     */
    @TableField("unload_car_num")
    private Integer unloadCarNum;

    /**
     * 实时运力ID
     */
    @TableField("vehicle_running_id")
    private Long vehicleRunningId;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 创建人名称
     */
    @TableField("create_user")
    private String createUser;

    /**
     * 创建人ID
     */
    @TableField("create_user_id")
    private Long createUserId;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Long createTime;


}
