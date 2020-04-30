package com.cjyc.foreign.api.dto.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

@Data
public class ReleaseCarReqDto {

    @NotBlank(message = "客户电话不能为空")
    @Pattern(regexp = "^[1]\\d{10}$", message = "请输入正确的手机号")
    @ApiModelProperty(value = "客户电话默认传16000000001", required = true)
    private String customerPhone;

    @NotNull(message = "处理类型")
    @ApiModelProperty(value = "0未付款不允许放车，1未付款允许放车，2已付款不允许放车，9已付款允许放车", required = true)
    private Integer type;

    @NotEmpty(message = "订单编号或者车辆编号列表不能为空")
    @ApiModelProperty(value = "订单编号或者车辆编号列表", required = true)
    private List<String> noList;

    @ApiModelProperty(value = "交易流水号")
    private String billNo;
}
