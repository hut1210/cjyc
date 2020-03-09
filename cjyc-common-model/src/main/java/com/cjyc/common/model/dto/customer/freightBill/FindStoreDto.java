package com.cjyc.common.model.dto.customer.freightBill;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Data
public class FindStoreDto implements Serializable {
    private static final long serialVersionUID = -6533219025534413601L;
    @ApiModelProperty(value = "城市编码",required = true)
    private String cityCode;

    @ApiModelProperty(value = "业务中心名称",required = true)
    private String name;

    @ApiModelProperty(value = "业务中心ids,不需要传")
    private Set<Long> storeIds;
}