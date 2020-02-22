package com.cjyc.common.model.dto.web.waybill;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
    @ApiModelProperty(value = "承运商名称", required = true)
    private String carrierName;

    @ApiModelProperty(value = "备注")
    private String remark;

    /**车辆信息*/
    @NotNull(message = "车辆ID不能为空")
    @ApiModelProperty(value = "订单车辆ID",required = true)
    private Long orderCarId;

    @NotBlank(message = "车辆编号不能为空")
    @ApiModelProperty(value = "车辆编号",required = true)
    private String orderCarNo;
    @NotBlank(message = "始发地区县不能为空")
    @ApiModelProperty(value = "区县编码",required = true)
    private String startAreaCode;
    @NotBlank(message = "始发地地址不能为空")
    @ApiModelProperty(value = "装车地址",required = true)
    private String startAddress;

    @ApiModelProperty(value = "出发地业务中心名称")
    private String startStoreName;

    @ApiModelProperty(value = "出发地业务中心ID")
    private Long startStoreId;

    @NotBlank(message = "目的地区县不能为空")
    @ApiModelProperty(value = "区县编码")
    private String endAreaCode;
    @NotBlank(message = "目的地地址不能为空")
    @ApiModelProperty(value = "卸车地址",required = true)
    private String endAddress;
    @ApiModelProperty(value = "目的地业务中心名称")
    private String endStoreName;

    @ApiModelProperty(value = "目的地业务中心ID")
    private Long endStoreId;
    @Min(value = 1, message = "预计提车日期不合规则")
    @ApiModelProperty(value = "预计提车日期",required = true)
    private Long expectStartTime;

    @ApiModelProperty(value = "预计到达时间")
    private Long expectEndTime;

    @ApiModelProperty(value = "提车联系人ID")
    private Long loadLinkUserId;
    @NotBlank(message = "提车联系人不能为空")
    @ApiModelProperty(value = "提车联系人")
    private String loadLinkName;
    @NotBlank(message = "提车联系人电话不能为空")
    @ApiModelProperty(value = "提车联系人电话")
    private String loadLinkPhone;

    @ApiModelProperty(value = "收车联系人userId")
    private Long unloadLinkUserId;
    @NotBlank(message = "收车人不能为空")
    @ApiModelProperty(value = "收车人名称")
    private String unloadLinkName;
    @NotBlank(message = "收车人不能为空")
    @ApiModelProperty(value = "收车人电话")
    private String unloadLinkPhone;

}
