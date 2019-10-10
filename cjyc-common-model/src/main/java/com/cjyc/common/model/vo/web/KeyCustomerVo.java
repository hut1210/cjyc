package com.cjyc.common.model.vo.web;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

@Data
public class KeyCustomerVo implements Serializable {

    public interface SaveKeyCustomerVo {
    }

    public interface UpdateKeyCustomerVo {
    }

    /**
     * 大客户主键id
     */
    private Long id;
    /**
     * 客户全称
     */
     @NotBlank(groups = {SaveKeyCustomerVo.class},message = "客户全称不能为空")
     @NotBlank(groups = {UpdateKeyCustomerVo.class},message = "客户全称不能为空")
     private String name;

    /**
     * 客户简称
     */
    @NotBlank(groups = {SaveKeyCustomerVo.class},message = "客户简称不能为空")
    @NotBlank(groups = {UpdateKeyCustomerVo.class},message = "客户简称不能为空")
    private String alias;

    /**
     * 联系人
     */
    @NotBlank(groups = {SaveKeyCustomerVo.class},message = "联系人不能为空")
    @NotBlank(groups = {UpdateKeyCustomerVo.class},message = "联系人不能为空")
    private String contactMan;

    /**
     * 联系电话
     */
    @NotBlank(groups = {SaveKeyCustomerVo.class},message = "联系电话不能为空")
    @NotBlank(groups = {UpdateKeyCustomerVo.class},message = "联系电话不能为空")
    private String phone;

    /**
     * 客户地址
     */
    private String contactAddress;

    /**
     * 客户性质
     */
    private String customerNature;

    /**
     * 公司性质/规模
     */
    private String companyNature;

    /**
     * 大客户合同
     */
    private List<CustomerContractVo> custContraVos;
}