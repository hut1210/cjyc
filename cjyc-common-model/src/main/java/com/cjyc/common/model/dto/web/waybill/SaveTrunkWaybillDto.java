package com.cjyc.common.model.dto.web.waybill;

import com.cjyc.common.model.constant.ArgsConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel
public class SaveTrunkWaybillDto {

    @NotNull(message = "loginId不能为空")
    @ApiModelProperty(value = "用户Id", required = true)
    private Long loginId;

    @ApiModelProperty(hidden = true)
    private String loginName;

    @ApiModelProperty(value = "指导线路")
    private String guideLine;

    @NotNull(message = "运费不能为空")
    @Digits(integer = ArgsConstant.INT_MAX, fraction = ArgsConstant.FRACTION_MAX, message = "金额整数最多8位，小数最多2位")
    @DecimalMin(value = ArgsConstant.DECIMAL_ZERO, message = "金额不能小于0")
    @ApiModelProperty(value = "运单总运费", required = true)
    private BigDecimal freightFee;

    @NotNull(message = "承运商不能为空")
    @ApiModelProperty(value = "承运商ID", required = true)
    private Long carrierId;

    @ApiModelProperty(value = "司机ID")
    private Long driverId;

    @NotNull(message = "是否包板不能为空")
    @ApiModelProperty(value = "运费是否固定（包板）0否，1是", required = true)
    private Boolean fixedFreightFee;

    @ApiModelProperty(value = "备注")
    private String remark;

    @NotEmpty(message = "车辆列表不能为空")
    @ApiModelProperty(value = "调度车辆列表", required = true)
    @Valid
    private List<SaveTrunkWaybillCarDto> list;
}
