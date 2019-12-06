package com.cjyc.common.model.vo.web.admin;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class TypeSalesmanVo implements Serializable {
    private static final long serialVersionUID = 3217035256635845269L;
    @ApiModelProperty("业务员id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long adminId;

    @ApiModelProperty("业务员姓名")
    private String name;

    @ApiModelProperty("业务员电话")
    private String phone;

    @ApiModelProperty("业务中心名称")
    private String storeName;
}