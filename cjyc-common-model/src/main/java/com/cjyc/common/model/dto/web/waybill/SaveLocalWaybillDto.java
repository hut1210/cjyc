package com.cjyc.common.model.dto.web.waybill;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

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
public class SaveLocalWaybillDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "承运商ID", required = true)
    private Long carrierId;

    @NotNull(message = "承运类型不能为空")
    @ApiModelProperty(value = "承运商类型：1干线-个人承运商，2干线-企业承运商，3同城-业务员，4同城-代驾，5同城-拖车，6客户自己", required = true)
    private Integer carrierType;

    @ApiModelProperty(value = "承运商名称")
    private String carrierName;

    @ApiModelProperty(value = "备注")
    private String remark;

    /**车辆信息*/
    @NotNull(message = "车辆ID不能为空")
    @ApiModelProperty(value = "订单车辆ID")
    private Long orderCarId;

    @NotBlank(message = "车辆编号不能为空")
    @ApiModelProperty(value = "车辆编号")
    private String orderCarNo;
    @NotBlank(message = "装车区县编码不能为空")
    @ApiModelProperty(value = "区县编码")
    private String startAreaCode;
    @NotBlank(message = "装车地址不能为空")
    @ApiModelProperty(value = "装车地址")
    private String startAddress;

    @ApiModelProperty(value = "出发地业务中心名称")
    private String startStoreName;

    @ApiModelProperty(value = "出发地业务中心ID")
    private Long startStoreId;

    @NotBlank(message = "卸车区县编码不能为空")
    @ApiModelProperty(value = "区县编码")
    private String endAreaCode;
    @NotBlank(message = "卸车地址不能为空")
    @ApiModelProperty(value = "卸车地址")
    private String endAddress;
    @ApiModelProperty(value = "目的地业务中心名称")
    private String endStoreName;

    @ApiModelProperty(value = "目的地业务中心ID")
    private Long endStoreId;
    @NotNull(message = "预计提车日期不能为空")
    @ApiModelProperty(value = "预计提车日期")
    private Long expectStartTime;

    @ApiModelProperty(value = "预计到达时间")
    private Long expectEndTime;

    @ApiModelProperty(value = "提车联系人")
    private String loadLinkName;

    @ApiModelProperty(value = "提车联系人ID")
    private Long loadLinkUserId;
    @ApiModelProperty(value = "提车联系人电话")
    private String loadLinkPhone;

    @ApiModelProperty(value = "收车人名称")
    private String unloadLinkName;

    @ApiModelProperty(value = "收车联系人userId")
    private Long unloadLinkUserId;
    @ApiModelProperty(value = "收车人电话")
    private String unloadLinkPhone;

    @ApiModelProperty(value = "最后一次运输标识：0否，1是")
    private Integer receiptFlag;



}
