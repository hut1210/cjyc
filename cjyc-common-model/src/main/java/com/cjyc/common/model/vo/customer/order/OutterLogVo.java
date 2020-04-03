package com.cjyc.common.model.vo.customer.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class OutterLogVo implements Serializable {
    private static final long serialVersionUID = 4188339848026602720L;
    @ApiModelProperty(value = "车辆编号")
    private String orderCarNo;
    @ApiModelProperty(value = "当前状态")
    private String outterState;
    @ApiModelProperty(value = "节点列表")
    List<OutterOrderCarLogVo> list;

    public String getOutterState() {
        return outterState == null ? "" : outterState;
    }
}
