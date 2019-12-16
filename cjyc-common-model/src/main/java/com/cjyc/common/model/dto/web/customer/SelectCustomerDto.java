package com.cjyc.common.model.dto.web.customer;

import com.cjyc.common.model.dto.BasePageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
@Data
@EqualsAndHashCode(callSuper = false)
public class SelectCustomerDto extends BasePageDto implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "标志 0:订单 1:客户")
    private Integer flag;

    @ApiModelProperty(value = "手机号(账号)")
    private String contactPhone;

    @ApiModelProperty(value = "姓名")
    private String contactMan;

    @ApiModelProperty(value = "身份证号")
    private String idCard;
}