package com.cjyc.common.model.dto.salesman.Admin;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class StoreAdminDto {

    @ApiModelProperty("业务中心ID")
    private Long storeId;
    @ApiModelProperty("查询类型：0默认查询（业务中心级别所有的角色）")
    private int queryType;
}
