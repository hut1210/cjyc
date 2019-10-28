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
public class LocalDispatchWaybillDto implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "车辆ID", required = true)
    private Long orderCarId;
    @ApiModelProperty(value = "车辆编号", required = true)
    private String orderCarNo;

    @ApiModelProperty(value = "装车地址", required = true)
    private String startAddress;

    @ApiModelProperty(value = "卸车地址", required = true)
    private String endAddress;

    @ApiModelProperty(value = "预计提车日期", required = true)
    private Long expectPickTime;

    @ApiModelProperty(value = "取车方式:1上门，2 自送/自取")
    private Integer takeType;

    @ApiModelProperty(value = "提车联系人")
    private String loadLinkName;

    @ApiModelProperty(value = "提车联系人userid")
    private Long loadLinkUserId;

    @ApiModelProperty(value = "提车联系人电话")
    private String loadLinkPhone;

    @ApiModelProperty(value = "收车人名称")
    private String unloadLinkName;

    @ApiModelProperty(value = "收车联系人userId")
    private Long unloadLinkUserId;

    @ApiModelProperty(value = "收车人电话")
    private String unloadLinkPhone;

    @ApiModelProperty(value = "承运商ID", required = true)
    private Long carrierId;

    @ApiModelProperty(value = "司机ID")
    private Long driverId;

    @ApiModelProperty(value = "备注")
    private String remark;


}
