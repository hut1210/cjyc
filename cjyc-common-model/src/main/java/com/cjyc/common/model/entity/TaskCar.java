package com.cjyc.common.model.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 任务明细表(车辆表)
 * </p>
 *
 * @author JPG
 * @since 2019-10-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("w_task_car")
public class TaskCar implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 任务ID
     */
    private Long taskId;

    /**
     * 运单ID
     */
    private Long waybillId;

    /**
     * 运单车辆ID
     */
    private Long waybillCarId;

    /**
     * 订单车辆ID
     */
    private String orderCarId;

    /**
     * 任务车辆状态：1待装车，2已装车，4已卸车，9确认收车
     */
    private Integer state;

    /**
     * 图片地址，逗号分隔
     */
    private String photoImg;

    /**
     * 装车时间
     */
    private Long loadTime;

    /**
     * 卸车时间
     */
    private Long unloadTime;

    /**
     * 车辆运费
     */
    private BigDecimal freightFee;

    /**
     * 创建时间
     */
    private Long createTime;


}
