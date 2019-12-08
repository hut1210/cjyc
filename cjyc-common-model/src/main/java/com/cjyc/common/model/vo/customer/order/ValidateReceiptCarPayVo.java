package com.cjyc.common.model.vo.customer.order;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ValidateReceiptCarPayVo {

    private Integer isNeedPay;
    private BigDecimal amount;
    private List<String> orderCarNos;
}
