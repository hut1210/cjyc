package com.cjyc.common.model.vo.web.line;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class LineExportExcel implements Serializable {

    private static final long serialVersionUID = 5003126840391965287L;
    @Excel(name = "始发省" ,orderNum = "0",width = 15)
    private String fromProvince;

    @Excel(name = "始发市" ,orderNum = "1",width = 15)
    private String fromCity;

    @Excel(name = "目的省" ,orderNum = "2",width = 15)
    private String toProvince;

    @Excel(name = "目的市" ,orderNum = "3",width = 15)
    private String toCity;

    @Excel(name = "物流费(元)" ,orderNum = "4",width = 15)
    private BigDecimal defaultWlFee;

    @Excel(name = "运费(元)" ,orderNum = "5",width = 15)
    private BigDecimal defaultFreightFee;

    @Excel(name = "总里程(km)" ,orderNum = "6",width = 15)
    private BigDecimal kilometer;

    @Excel(name = "总里程(天)" ,orderNum = "7",width = 15)
    private Integer days;

    @Excel(name = "备注" ,orderNum = "8",width = 15)
    private String remark;

    @Excel(name = "创建时间" ,orderNum = "9",width = 15)
    private String createTime;

    @Excel(name = "创建人" ,orderNum = "10",width = 15)
    private String createUserName;

    public String getDefaultWlFee() {
        return defaultWlFee == null ? "0.00" : defaultWlFee.divide(new BigDecimal(100)).toString()+".00";
    }

    public String getDefaultFreightFee() {
        return defaultFreightFee == null ? "0.00" : defaultFreightFee.divide(new BigDecimal(100)).toString()+".00";
    }
}