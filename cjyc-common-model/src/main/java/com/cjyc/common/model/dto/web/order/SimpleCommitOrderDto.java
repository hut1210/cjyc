package com.cjyc.common.model.dto.web.order;

import com.cjyc.common.model.enums.UserTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SimpleCommitOrderDto {

    @NotNull(message = "loginId不能为空")
    @ApiModelProperty(value = "操作人id", required = true)
    private Long loginId;
    @ApiModelProperty(hidden = true)
    private String loginName;
    @ApiModelProperty(hidden = true)
    private String loginPhone;
    @ApiModelProperty(value = "操作人类型", hidden = true)
    private UserTypeEnum loginType;

    @NotNull(message = "订单ID")
    @ApiModelProperty(value = "订单ID",required = true)
    private Integer orderId;

}
