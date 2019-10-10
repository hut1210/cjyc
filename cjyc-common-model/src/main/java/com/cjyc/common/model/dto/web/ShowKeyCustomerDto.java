package com.cjyc.common.model.dto.web;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ShowKeyCustomerDto implements Serializable {

    /**
     * 大客户主键id
     */
    private Long id;

    /**
     * 客户全称
     */
     private String name;

    /**
     * 客户简称
     */
    private String alias;

    /**
     * 联系人
     */
    private String contactMan;

    /**
     * 联系电话
     */
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
    private List<CustomerContractDto> custContraVos;
}