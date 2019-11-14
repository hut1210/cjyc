package com.cjyc.common.model.vo.web.admin;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class TypeSalesmanVo implements Serializable {

    @ApiModelProperty("业务员id")
    private Long adminId;

    @ApiModelProperty("业务员姓名")
    private String name;

    @ApiModelProperty("业务员电话")
    private String phone;

    @ApiModelProperty("业务中心名称")
    private String storeName;
}