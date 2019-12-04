package com.cjyc.common.model.dto.customer;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UpdateCustomerDto {

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "联系人名称")
    private String contactMan;

    @ApiModelProperty(value = "客户地址")
    private String contactAddress;

    @ApiModelProperty(value = "性别")
    private Integer sex;

    @ApiModelProperty(value = "身份证号")
    private String idCard;

    @ApiModelProperty(value = "身份证正面")
    private String idCardFrontImg;

    @ApiModelProperty(value = "身份证反面")
    private String idCardBackImg;


}
