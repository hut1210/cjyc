package com.cjyc.common.model.vo.salesman.dispatch;

import com.cjyc.common.model.serizlizer.BigDecimalSerizlizer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ValidateLineVo {
    private String orderCarNo;
    private Boolean hasLine;
    private Long lineId;
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal lineFreightFee;
}
