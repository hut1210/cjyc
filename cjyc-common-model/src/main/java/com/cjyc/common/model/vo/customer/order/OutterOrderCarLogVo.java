package com.cjyc.common.model.vo.customer.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OutterOrderCarLogVo {
    @ApiModelProperty(value = "是否为实时位置：1-是；0-否")
    private int tag = 0;

    @ApiModelProperty(value = "车辆编号")
    private String orderCarNo;

    @ApiModelProperty(value = "日志类型")
    private String typeStr;

    @ApiModelProperty(value = "外部日志/实时位置")
    private String outerLog;

    @ApiModelProperty(value = "创建时间/终端定位时间")
    private Long createTime;

    public String getOrderCarNo() {
        return orderCarNo == null ? "" : orderCarNo;
    }

    public String getTypeStr() {
        return typeStr == null ? "" : typeStr;
    }

    public String getOuterLog() {
        return outerLog == null ? "" : outerLog;
    }

    public Long getCreateTime() {
        return createTime == null ? 0 : createTime;
    }
}
