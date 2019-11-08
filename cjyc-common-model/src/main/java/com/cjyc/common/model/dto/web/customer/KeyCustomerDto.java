package com.cjyc.common.model.dto.web.customer;

import com.cjyc.common.model.dto.web.customer.CustomerContractDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
public class KeyCustomerDto implements Serializable {

    public interface SaveKeyCustomerVo {
    }

    public interface UpdateKeyCustomerVo {
    }

    @NotNull(groups = {SaveKeyCustomerVo.class},message = "登陆用户id(loginId)不能为空")
    @ApiModelProperty(value = "登陆用户id(loginId)")
    private Long loginId;

    @ApiModelProperty(value = "大客户主键id(customerId)")
    @NotNull(groups = {UpdateKeyCustomerVo.class},message = "大客户主键id(customerId)不能为空")
    private Long customerId;

     @NotBlank(groups = {SaveKeyCustomerVo.class},message = "客户全称不能为空")
     @NotBlank(groups = {UpdateKeyCustomerVo.class},message = "客户全称不能为空")
     @ApiModelProperty(value = "客户全称",required = true)
     private String name;

     @ApiModelProperty("统一社会信用代码")
     @NotBlank(groups = {SaveKeyCustomerVo.class},message = "统一社会信用代码不能为空")
     @NotBlank(groups = {UpdateKeyCustomerVo.class},message = "统一社会信用代码不能为空")
     private String socialCreditCode;

    @NotBlank(groups = {SaveKeyCustomerVo.class},message = "联系人不能为空")
    @NotBlank(groups = {UpdateKeyCustomerVo.class},message = "联系人不能为空")
    @ApiModelProperty(value = "联系人",required = true)
    private String contactMan;

    @NotBlank(groups = {SaveKeyCustomerVo.class},message = "联系电话不能为空")
    @NotBlank(groups = {UpdateKeyCustomerVo.class},message = "联系电话不能为空")
    @ApiModelProperty(value = "联系电话",required = true)
    private String contactPhone;

    @ApiModelProperty(value = "客户地址")
    private String contactAddress;

    @ApiModelProperty(value = "客户性质  0：电商 1：租赁 2：金融公司 3：经销商 4：其他")
    private Integer customerNature;

    @ApiModelProperty(value = "大客户合同")
    @NotEmpty(message = "合同不能为空")
    private List<CustomerContractDto> custContraVos;
}