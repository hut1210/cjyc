package com.cjyc.common.model.vo.web;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ShowKeyCustomerVo implements Serializable {

    @ApiModelProperty(value = "大客户主键id")
    private Long id;

    @ApiModelProperty(value = "客户全称")
    private String name;

    @ApiModelProperty(value = "客户简称")
    private String alias;

    @ApiModelProperty(value = "联系人")
    private String contactMan;

    @ApiModelProperty(value = "联系电话")
    private String phone;

    @ApiModelProperty(value = "客户地址")
    private String contactAddress;

    @ApiModelProperty(value = "客户性质")
    private String customerNature;

    @ApiModelProperty(value = "公司性质/规模")
    private String companyNature;

    @ApiModelProperty(value = "大客户合同")
    private List<CustomerContractVo> custContraVos;
}