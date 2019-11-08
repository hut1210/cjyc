package com.cjyc.common.model.dto.web.customer;

import com.cjyc.common.model.dto.BasePageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SelectKeyCustomerDto extends BasePageDto implements Serializable {

    @ApiModelProperty(value = "大客户编号")
    private String customerNo;

    @ApiModelProperty(value = "大客户全称")
    private String name;

    @ApiModelProperty(value = "大客户简称")
    private String alias;

    @ApiModelProperty(value = "联系人")
    private String contactMan;

    @ApiModelProperty(value = "联系电话")
    private String contactPhone;

    @ApiModelProperty("客户类型  0：电商 1：租赁 2：金融公司 3：经销商 4：其他")
    private Integer customerNature;

    @ApiModelProperty(value = "创建人")
    private String createUserName;
}