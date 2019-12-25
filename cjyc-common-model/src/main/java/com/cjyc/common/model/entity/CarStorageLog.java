package com.cjyc.common.model.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 出入库记录
 * </p>
 *
 * @author JPG
 * @since 2019-11-22
 */
@Builder
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("w_car_storage_log")
@ApiModel
public class CarStorageLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "业务中心ID")
    private Long storeId;

    @ApiModelProperty(value = "记录类型：1入库，2出库")
    private Integer type;

    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "车辆编号")
    private String orderCarNo;

    @ApiModelProperty(value = "车架号")
    private String vin;

    @ApiModelProperty(value = "品牌")
    private String brand;

    @ApiModelProperty(value = "型号")
    private String model;

    @ApiModelProperty(value = "运费")
    private BigDecimal freight;

    @ApiModelProperty(value = "承运方式")
    private Integer carryType;

    @ApiModelProperty(value = "承运商ID")
    private Long carrierId;

    @ApiModelProperty(value = "承运商")
    private String carrier;

    @ApiModelProperty(value = "司机")
    private String driver;

    @ApiModelProperty(value = "司机ID")
    private Long driverId;

    @ApiModelProperty(value = "司机手机号")
    private String drvierPhone;

    @ApiModelProperty(value = "司机车牌号")
    private String vehiclePlateNo;

    @ApiModelProperty(value = "创建时间")
    private Long createTime;

    @ApiModelProperty(value = "创建人ID")
    private Long createUserId;

    @ApiModelProperty(value = "创建人")
    private String createUser;


}
