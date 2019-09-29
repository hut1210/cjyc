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
 * 承运商/司机关系表
 * </p>
 *
 * @author JPG
 * @since 2019-09-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("d_carrier_driver_con")
public class CarrierDriverCon implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    /**
     * 承运商ID
     */
    @TableField("carrier_id")
    private Long carrierId;

    /**
     * 司机ID
     */
    @TableField("driver_id")
    private Long driverId;

    /**
     * 角色：0普通司机，1管理员，2超级管理员
     */
    @TableField("role")
    private Integer role;


}
