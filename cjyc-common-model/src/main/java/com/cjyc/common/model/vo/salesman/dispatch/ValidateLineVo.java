package com.cjyc.common.model.vo.salesman.dispatch;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ValidateLineVo {
    private String orderCarNo;
    private Boolean hasLine;
    private Long lineId;
    private BigDecimal lineFreightFee;
}
