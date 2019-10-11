package com.cjyc.common.model.dto.web;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

@Data
public class KeyCustomerDto implements Serializable {

    public interface SaveKeyCustomerVo {
    }

    public interface UpdateKeyCustomerVo {
    }

    @ApiModelProperty(value = "大客户主键id")
    private Long id;

     @NotBlank(groups = {SaveKeyCustomerVo.class},message = "客户全称不能为空")
     @NotBlank(groups = {UpdateKeyCustomerVo.class},message = "客户全称不能为空")
     @ApiModelProperty(value = "客户全称",required = true)
     private String name;

    @NotBlank(groups = {SaveKeyCustomerVo.class},message = "客户简称不能为空")
    @NotBlank(groups = {UpdateKeyCustomerVo.class},message = "客户简称不能为空")
    @ApiModelProperty(value = "客户简称",required = true)
    private String alias;

    @NotBlank(groups = {SaveKeyCustomerVo.class},message = "联系人不能为空")
    @NotBlank(groups = {UpdateKeyCustomerVo.class},message = "联系人不能为空")
    @ApiModelProperty(value = "联系人",required = true)
    private String contactMan;

    @NotBlank(groups = {SaveKeyCustomerVo.class},message = "联系电话不能为空")
    @NotBlank(groups = {UpdateKeyCustomerVo.class},message = "联系电话不能为空")
    @ApiModelProperty(value = "联系电话",required = true)
    private String phone;

    @ApiModelProperty(value = "客户地址")
    private String contactAddress;

    @ApiModelProperty(value = "客户性质")
    private String customerNature;

    @ApiModelProperty(value = "公司性质/规模")
    private String companyNature;

    @ApiModelProperty(value = "大客户合同")
    private List<CustomerContractDto> custContraVos;
}