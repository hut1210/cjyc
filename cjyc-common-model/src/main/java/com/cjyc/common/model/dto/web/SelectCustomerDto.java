package com.cjyc.common.model.dto.web;

import com.cjyc.common.model.dto.BasePageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class SelectCustomerDto extends BasePageDto implements Serializable {

    @ApiModelProperty(value = "手机号(账号)")
    private String phone;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "身份证号")
    private String idCard;
}