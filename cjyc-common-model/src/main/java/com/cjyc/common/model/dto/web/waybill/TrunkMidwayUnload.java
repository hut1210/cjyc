package com.cjyc.common.model.dto.web.waybill;

import com.cjyc.common.model.constant.ArgsConstant;
import com.cjyc.common.model.dto.web.BaseWebDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

@Data
public class TrunkMidwayUnload extends BaseWebDto {

    @NotNull(message = "运单ID不能为空")
    @ApiModelProperty(value = "运单ID")
    private String waybillId;
    @NotBlank(message = "区县编码不能为空")
    @ApiModelProperty(value = "区县编码")
    private String endAreaCode;
    @NotBlank(message = "卸车地址不能为空")
    @ApiModelProperty(value = "卸车地址")
    private String endAddress;

    @ApiModelProperty(value = "目的地业务中心名称")
    private String endStoreName;

    @ApiModelProperty(value = "目的地业务中心ID")
    private Long endStoreId;
    @NotBlank(message = "收车人名称不能为空")
    @ApiModelProperty(value = "收车人名称")
    private String unloadLinkName;

    @ApiModelProperty(value = "收车联系人userId")
    private Long unloadLinkUserId;

    @NotBlank(message = "收车人电话不能为空")
    @ApiModelProperty(value = "收车人电话")
    @Pattern(regexp = "^[1]\\d{10}$", message = "收车人电话格式不正确")
    private String unloadLinkPhone;

    @NotNull(message = "收卸车时间不能为空")
    @ApiModelProperty(value = "实际完成卸车时间")
    private Long unloadTime;

    @NotNull(message = "运费不能为空")
    @ApiModelProperty(value = "运费")
    @DecimalMax(value = ArgsConstant.DECIMAL_MAX, message = "金额不能超过99999999.99")
    @DecimalMin(value = ArgsConstant.DECIMAL_ZERO, message = "金额不能小于0")
    private BigDecimal freightFee;

    @NotEmpty(message = "车辆不能为空")
    @ApiModelProperty(value = "运单车辆ID列表")
    List<Long> waybillCarIdList;
}
