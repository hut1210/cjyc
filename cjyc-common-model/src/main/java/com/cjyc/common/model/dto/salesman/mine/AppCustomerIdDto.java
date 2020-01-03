package com.cjyc.common.model.dto.salesman.mine;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class AppCustomerIdDto implements Serializable {
    private static final long serialVersionUID = -8416203728082858684L;

    @ApiModelProperty(value = "大客户id")
    private Long customerId;
}