package com.cjyc.common.model.vo.web.customer;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class CustomerInfoVo implements Serializable {
    private static final long serialVersionUID = 8109061584689019071L;

    @ApiModelProperty("客户id")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long customerId;

    @ApiModelProperty("客户姓名")
    private String contactMan;

    @ApiModelProperty("客户手机号")
    private String contactPhone;
}