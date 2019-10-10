package com.cjyc.common.model.vo.web;

import com.cjyc.common.model.vo.BasePageVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SelectKeyCustomerVo extends BasePageVo implements Serializable {

    @ApiModelProperty(value = "大客户主键id")
    private Long id;

    @ApiModelProperty(value = "大客户全称")
    private String name;

    @ApiModelProperty(value = "大客户简称")
    private String alias;

    @ApiModelProperty(value = "联系人")
    private String contactMan;

    @ApiModelProperty(value = "联系电话")
    private String phone;

    @ApiModelProperty(value = "创建人")
    private String createUserId;

}