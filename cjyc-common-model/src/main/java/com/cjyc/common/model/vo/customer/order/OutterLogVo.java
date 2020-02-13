package com.cjyc.common.model.vo.customer.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class OutterLogVo {
    @ApiModelProperty(value = "状态")
    private String outterState;
    @ApiModelProperty(value = "节点列表")
    List<OutterOrderCarLogVo> list;

}
