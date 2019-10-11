package com.cjyc.common.model.vo.web;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class ListKeyCustomerVo implements Serializable {

    @ApiModelProperty(value = "大客户id")
    private String id;

    @ApiModelProperty(value = "大客户客户全称")
    private String name;

    @ApiModelProperty(value = "客户简称")
    private String alias;

    @ApiModelProperty(value = "联系人")
    private String contactMan;

    @ApiModelProperty(value = "联系电话")
    private String phone;

    @ApiModelProperty(value = "客户性质")
    private String customerNature;

    @ApiModelProperty(value = "主营业务描述")
    private String majorBusDes;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "创建人")
    private String createUserId;
}