package com.cjyc.common.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2019-10-08
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
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 司机ID
     */
    private Long driverId;

    /**
     * 运力ID
     */
    private Long vehicleId;

    /**
     * 运力车牌
     */
    private String vehicleNo;

    /**
     * 承运数
     */
    private Integer carryCarNum;

    /**
     * 空车位
     */
    private Integer emptyCarNum;

    /**
     * 状态：0无效，1有效
     */
    private Integer state;

    /**
     * 运行状态：0空闲，1在途
     */
    private Integer runningState;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 更新时间
     */
    private Long updateTime;

    /**
     * 上报时间
     */
    private String beatTime;

    /**
     * 经度
     */
    private String lng;

    /**
     * 纬度
     */
    private String lat;


}
