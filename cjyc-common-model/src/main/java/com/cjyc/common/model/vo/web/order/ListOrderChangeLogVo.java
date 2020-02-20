package com.cjyc.common.model.vo.web.order;

import com.cjyc.common.model.entity.OrderChangeLog;
import com.cjyc.common.model.serizlizer.BigDecimalSerizlizer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ListOrderChangeLogVo extends OrderChangeLog {
    @ApiModelProperty("退款金额")
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal totalfee;
    @ApiModelProperty("退款金额")
    private BigDecimal payType;
}
