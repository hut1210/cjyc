package com.cjyc.common.model.vo.web.customer;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ShowKeyCustomerVo implements Serializable {

    @ApiModelProperty(value = "大客户主键id")
    private Long id;

    @ApiModelProperty("大客户userId")
    private Long userId;

    @ApiModelProperty(value = "客户全称")
    private String name;

    @ApiModelProperty(value = "统一社会代码")
    private String socialCreditCode;

    @ApiModelProperty(value = "业务对接人")
    private String contactMan;

    @ApiModelProperty(value = "联系电话")
    private String contactPhone;

    @ApiModelProperty(value = "客户地址")
    private String contactAddress;

    @ApiModelProperty(value = "客户性质  0：电商 1：租赁 2：金融公司 3：经销商 4：其他")
    private Integer customerNature;

    @ApiModelProperty(value = "大客户合同")
    private List<CustomerContractVo> custContraVos;
}