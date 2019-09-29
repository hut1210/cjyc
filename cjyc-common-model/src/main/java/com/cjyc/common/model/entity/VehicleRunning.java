package com.cjyc.common.model.entity;

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
 * 运行运力池表
 * </p>
 *
 * @author JPG
 * @since 2019-09-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("d_vehicle_running")
public class VehicleRunning implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    /**
     * 司机ID
     */
    @TableField("driver_id")
    private Long driverId;

    /**
     * 运力ID
     */
    @TableField("vehicle_id")
    private Long vehicleId;

    /**
     * 运力车牌
     */
    @TableField("vehicle_no")
    private String vehicleNo;

    /**
     * 承运数
     */
    @TableField("carry_car_num")
    private Integer carryCarNum;

    /**
     * 空车位
     */
    @TableField("empty_car_num")
    private Integer emptyCarNum;

    /**
     * 状态：0无效，1有效
     */
    @TableField("state")
    private Integer state;

    /**
     * 运行状态：0空闲，1在途
     */
    @TableField("running_state")
    private Integer runningState;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Long createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private Long updateTime;

    /**
     * 上报时间
     */
    @TableField("beat_time")
    private String beatTime;

    /**
     * 经度
     */
    @TableField("lng")
    private String lng;

    /**
     * 纬度
     */
    @TableField("lat")
    private String lat;


}
