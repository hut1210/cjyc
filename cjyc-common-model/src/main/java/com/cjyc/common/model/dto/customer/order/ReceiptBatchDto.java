package com.cjyc.common.model.dto.customer.order;

import com.cjyc.common.model.enums.UserTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ReceiptBatchDto {

    @NotNull(message = "loginId不能为空")
    @ApiModelProperty(value = "用户Id",required = true)
    private Long loginId;
    @ApiModelProperty(value = "用户名称（不用传）")
    private String loginName;
    @ApiModelProperty(value = "用户手机号（不用传）")
    private String loginPhone;
    @ApiModelProperty(value = "用户类型(不用传)")
    private UserTypeEnum loginType;
    @NotEmpty
    @ApiModelProperty(value = "订单车辆编号",required = true)
    private List<String> orderCarNos;
}
