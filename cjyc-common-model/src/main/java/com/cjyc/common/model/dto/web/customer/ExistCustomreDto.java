package com.cjyc.common.model.dto.web.customer;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
@Data
public class ExistCustomreDto implements Serializable {
    private static final long serialVersionUID = 6452401960061880843L;

    @ApiModelProperty("客户id(customerId)，新增时不需要传，修改时要传")
    private Long customerId;

    @ApiModelProperty("客户手机号")
    @NotBlank(message = "客户手机号不能未空")
    private String phone;

}