package com.cjyc.common.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 承运商/司机关系表
 * </p>
 *
 * @author JPG
 * @since 2019-10-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("d_carrier_driver_con")
@ApiModel(value="CarrierDriverCon对象", description="承运商/司机关系表")
public class CarrierDriverCon implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "承运商ID")
    private Long carrierId;

    @ApiModelProperty(value = "司机ID")
    private Long driverId;

    @ApiModelProperty("承运方式：2 : 代驾  3 : 干线   4：拖车")
    private Integer mode;

    @ApiModelProperty(value = "状态：0待审核，2已审核，4取消，5冻结  7已驳回，9已停用（CommonStateEnum）")
    private Integer state;

    @ApiModelProperty(value = "角色：0个人司机，1下属司机，2管理员，3超级管理员")
    private Integer role;


}
