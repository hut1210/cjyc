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
 * 车辆物流轨迹
 * </p>
 *
 * @author JPG
 * @since 2019-09-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("w_order_car_log")
public class OrderCarLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableField("id")
    private Long id;

    /**
     * 订单车辆ID
     */
    @TableField("order_car_id")
    private Long orderCarId;

    /**
     * 内部日志
     */
    @TableField("inner_log")
    private String innerLog;

    /**
     * 外部日志
     */
    @TableField("outer_log")
    private String outerLog;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Long createTime;


}
