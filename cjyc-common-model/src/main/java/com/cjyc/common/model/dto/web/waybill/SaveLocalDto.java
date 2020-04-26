package com.cjyc.common.model.dto.web.waybill;

import com.cjyc.common.model.constant.ArgsConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel
@Validated
public class SaveLocalDto {

    @NotNull(message = "登录人不能为空")
    @ApiModelProperty(value = "用户Id", required = true)
    private Long loginId;

    @ApiModelProperty(hidden = true)
    private String loginName;

    @NotNull(message = "调度类型不能为空")
    @ApiModelProperty(value = "运单类型：1提车运单，2干线运单，3送车运单", required = true)
    private Integer type;

    @ApiModelProperty(value = "运费", required = true)
    @Digits(integer = ArgsConstant.INT_MAX, fraction = ArgsConstant.FRACTION_MAX, message = "金额整数最多8位，小数最多2位")
    @DecimalMin(value = ArgsConstant.DECIMAL_ZERO, message = "金额不能小于0")
    private BigDecimal freightFee;


    @NotEmpty(message = "调度内容不能为空")
    @ApiModelProperty(value = "调度运单列表", required = true)
    @Valid
    private List<SaveLocalWaybillDto> list;
}
