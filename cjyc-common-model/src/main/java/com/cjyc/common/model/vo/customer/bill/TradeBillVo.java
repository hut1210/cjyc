package com.cjyc.common.model.vo.customer.bill;

import com.cjyc.common.model.entity.TradeBill;
import lombok.Data;

import java.util.List;

@Data
public class TradeBillVo extends TradeBill {
    private List<String> sourceNos;

}
