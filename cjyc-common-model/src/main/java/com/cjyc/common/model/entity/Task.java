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
 * 任务表(子运单)
 * </p>
 *
 * @author JPG
 * @since 2019-10-08
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
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 任务编号
     */
    private String no;

    /**
     * 运单ID
     */
    private Long waybillId;

    /**
     * 车辆数
     */
    private Integer carNum;

    /**
     * 任务状态：0待承接，5待装车，10运输中，90部分完成，100已完成，102已取消，103已拒接
     */
    private Integer state;

    /**
     * 司机名称
     */
    private Integer driverName;

    /**
     * 司机ID
     */
    private Long driverId;

    /**
     * 装车数量
     */
    private Integer loadCarNum;

    /**
     * 卸车数量
     */
    private Integer unloadCarNum;

    /**
     * 实时运力ID
     */
    private Long vehicleRunningId;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建人名称
     */
    private String createUser;

    /**
     * 创建人ID
     */
    private Long createUserId;

    /**
     * 创建时间
     */
    private Long createTime;


}
