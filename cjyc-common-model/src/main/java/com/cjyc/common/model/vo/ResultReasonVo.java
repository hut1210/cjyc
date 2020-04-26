package com.cjyc.common.model.vo;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Collection;

@Data
public class ResultReasonVo {
    @ApiModelProperty("成功列表")
    private Collection<String> successList;
    @ApiModelProperty("失败列表及原因")
    private Collection<FailResultReasonVo> failList = Lists.newArrayList();
}
