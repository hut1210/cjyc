package com.cjyc.common.model.vo.salesman.customer;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SalesKeyCustomerListVo implements Serializable {
    private static final long serialVersionUID = 4594926807110143288L;

    @ApiModelProperty(value = "客户信息")
    private List<SalesKeyCustomerVo> list;
}