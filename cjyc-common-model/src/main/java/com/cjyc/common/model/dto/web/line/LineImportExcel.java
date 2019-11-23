package com.cjyc.common.model.dto.web.line;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class LineImportExcel implements Serializable {

    private static final long serialVersionUID = -5714061256079532395L;
    @Excel(name = "始发市" ,orderNum = "0")
    private String fromCity;

    @Excel(name = "目的市" ,orderNum = "1")
    private String toCity;

    @Excel(name = "物流费" ,orderNum = "2")
    private BigDecimal defaultWlFee;

    @Excel(name = "运费" ,orderNum = "3")
    private BigDecimal defaultFreightFee;

    @Excel(name = "总里程(km)" ,orderNum = "4",width = 15)
    private BigDecimal kilometer;

    @Excel(name = "总耗时(天)" ,orderNum = "5",width = 15)
    private Integer days;
}