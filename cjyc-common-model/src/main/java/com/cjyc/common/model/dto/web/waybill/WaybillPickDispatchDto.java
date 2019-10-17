package com.cjyc.common.model.dto.web.waybill;

import java.math.BigDecimal;

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
 * 运单表(业务员调度单)
 * </p>
 *
 * @author JPG
 * @since 2019-10-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel
public class WaybillPickDispatchDto implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "车辆ID")
    private Long orderCarId;
    @ApiModelProperty(value = "车辆编号")
    private String orderCarNo;

    @ApiModelProperty(value = "装车地址")
    private String startAddress;

    @ApiModelProperty(value = "卸车地址")
    private String endAddress;

    @ApiModelProperty(value = "预计提车日期")
    private Long expectPickTime;

    @ApiModelProperty(value = "提车方式:1 自送，2上门")
    private Integer pickType;

    @ApiModelProperty(value = "提车联系人")
    private String pickContactName;

    @ApiModelProperty(value = "提车联系人电话")
    private String pickContactPhone;


    @ApiModelProperty(value = "承运商ID")
    private Long carrierId;


}
