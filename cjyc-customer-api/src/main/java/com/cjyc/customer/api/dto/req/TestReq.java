package com.cjyc.customer.api.dto.req;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * Created by leo on 2019/7/23.
 */
public class TestReq implements BaseReq{
    private static final long serialVersionUID = 1L;

    @NotNull
    @ApiModelProperty(value = "测试字符串1",example="mockStrValue")
    private String strDemo;

    @NotNull
    @ApiModelProperty(value = "测试字符串2",example="1234343523",required = true)
    private Long longNum;
}
