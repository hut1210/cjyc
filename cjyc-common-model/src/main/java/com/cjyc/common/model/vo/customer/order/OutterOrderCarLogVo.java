package com.cjyc.common.model.vo.customer.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OutterOrderCarLogVo {

    @ApiModelProperty(value = "车辆编号")
    private String orderCarNo;

    @ApiModelProperty(value = "日志类型")
    private String typeStr;

    @ApiModelProperty(value = "外部日志")
    private String outerLog;

    @ApiModelProperty(value = "创建时间")
    private Long createTime;


}
