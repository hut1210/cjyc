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
 * 承运商分表
 * </p>
 *
 * @author JPG
 * @since 2019-10-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("d_carrier_point")
public class CarrierPoint implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 司机ID
     */
    private Long carrierId;

    /**
     * 锁定积分
     */
    private Integer protectedPoint;

    /**
     * 可用积分
     */
    private Integer availablePoint;

    /**
     * 状态：
     */
    private Integer state;

    /**
     * 总收入积分
     */
    private Integer totalIncomePoint;

    /**
     * 总花费积分
     */
    private Integer totalExpendPoint;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新时间
     */
    private String updateTime;


}
