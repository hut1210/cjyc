package com.cjyc.common.model.dto.web.line;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
@Data
public class SaveOrUpdateLineExcel implements Serializable {
    private static final long serialVersionUID = -5112986928562564635L;

    @Excel(name = "始发省" ,orderNum = "0")
    private String fromProvince;

    @Excel(name = "始发市" ,orderNum = "1")
    private String fromCity;

    @Excel(name = "目的省" ,orderNum = "2")
    private String toProvince;

    @Excel(name = "目的市" ,orderNum = "3")
    private String toCity;

    @Excel(name = "物流费/元（上游）含税" ,orderNum = "4")
    private String defaultWlFee;

    @Excel(name = "物流费/元（上游）不含税" ,orderNum = "5")
    private String noTaxWlFee;

    @Excel(name = "运费/元（下游）" ,orderNum = "6")
    private String defaultFreightFee;

    @Excel(name = "总耗时(天)" ,orderNum = "7")
    private Integer days;
}