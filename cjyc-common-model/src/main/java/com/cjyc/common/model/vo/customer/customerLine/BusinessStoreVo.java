package com.cjyc.common.model.vo.customer.customerLine;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class BusinessStoreVo implements Serializable {
    private static final long serialVersionUID = 8877492089564701830L;
    @ApiModelProperty("业务中心id")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long storeId;

    @ApiModelProperty("业务中心名称")
    private String name;

    @ApiModelProperty("详细地址")
    private String detailAddr;
}