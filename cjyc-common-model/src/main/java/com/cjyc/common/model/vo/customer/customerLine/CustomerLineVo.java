package com.cjyc.common.model.vo.customer.customerLine;

import com.cjyc.common.model.dto.BasePageDto;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class CustomerLineVo implements Serializable {
    private static final long serialVersionUID = 2120381343345734965L;

    @ApiModelProperty("线路id")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long id;

    @ApiModelProperty("起始地址")
    private String startAdress;

    @ApiModelProperty("起始地联系人")
    private String startContact;

    @ApiModelProperty("起始地联系人电话")
    private String startContactPhone;

    @ApiModelProperty("目的地地址")
    private String endAdress;

    @ApiModelProperty("目的地联系人")
    private String endContact;

    @ApiModelProperty("目的地联系人电话")
    private String endContactPhone;
}