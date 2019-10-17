package com.cjyc.common.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class BaseTipVo {

    @ApiModelProperty("返回信息编号")
    private String no;
    @ApiModelProperty("返回信息提示")
    private String tip;
}
