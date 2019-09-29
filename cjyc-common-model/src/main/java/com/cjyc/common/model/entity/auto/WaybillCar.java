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
 * 运单明细表(车辆表)
 * </p>
 *
 * @author JPG
 * @since 2019-09-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("w_waybill_car")
public class WaybillCar implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    /**
     * 运单ID
     */
    @TableField("waybill_id")
    private Long waybillId;

    /**
     * 订单车辆ID
     */
    @TableField("order_car_id")
    private Long orderCarId;

    /**
     * 运费
     */
    @TableField("freight_fee")
    private BigDecimal freightFee;

    /**
     * 装车地址
     */
    @TableField("start_address")
    private String startAddress;

    /**
     * 卸车地址
     */
    @TableField("end_address")
    private String endAddress;

    /**
     * 装车区县编码
     */
    @TableField("start_area_code")
    private String startAreaCode;

    /**
     * 卸车区县编码
     */
    @TableField("end_area_code")
    private String endAreaCode;

    /**
     * 状态：1待指派，3已指派，9已完成
     */
    @TableField("state")
    private Integer state;


}
