package com.cjyc.common.model.entity.auto;

import java.math.BigDecimal;
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
 * 任务明细表(车辆表)
 * </p>
 *
 * @author JPG
 * @since 2019-09-29
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
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    /**
     * 任务ID
     */
    @TableField("task_id")
    private Long taskId;

    /**
     * 运单ID
     */
    @TableField("waybill_id")
    private Long waybillId;

    /**
     * 运单车辆ID
     */
    @TableField("waybill_car_id")
    private Long waybillCarId;

    /**
     * 订单车辆ID
     */
    @TableField("order_car_id")
    private String orderCarId;

    /**
     * 任务车辆状态：1待装车，2已装车，4已卸车，9确认收车
     */
    @TableField("state")
    private Integer state;

    /**
     * 图片地址，逗号分隔
     */
    @TableField("photo_img")
    private String photoImg;

    /**
     * 车辆运费
     */
    @TableField("freight_fee")
    private BigDecimal freightFee;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Long createTime;


}
