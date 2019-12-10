package com.cjyc.common.model.dto.salesman.customer;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class SalesCustomerDto implements Serializable {
    private static final long serialVersionUID = -3428558475981907414L;
    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "姓名")
    private String name;
}