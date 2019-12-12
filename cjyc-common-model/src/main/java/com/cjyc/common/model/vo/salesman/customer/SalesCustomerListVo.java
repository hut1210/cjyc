package com.cjyc.common.model.vo.salesman.customer;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SalesCustomerListVo implements Serializable {
    private static final long serialVersionUID = 172958856851958843L;
    @ApiModelProperty(value = "客户信息")
    private List<SalesCustomerVo> list;
}