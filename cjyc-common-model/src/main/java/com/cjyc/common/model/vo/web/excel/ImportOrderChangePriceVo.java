package com.cjyc.common.model.vo.web.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.cjyc.common.model.serizlizer.BigDecimalSerizlizer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ImportOrderChangePriceVo {

    @Excel(name = "订单编号", orderNum = "0")
    private String no;

    @Excel(name = "客户名称", orderNum = "1")
    private String customerName;

    private String customerTypeStr;

    @Excel(name = "修改前服务费", orderNum = "6")
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal oldAgencyFee;
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    @Excel(name = "修改前物流费", orderNum = "7")
    private BigDecimal oldWlTotalFee;
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    @Excel(name = "修改前订单金额", orderNum = "8")
    private BigDecimal oldTotalFee;

    @Excel(name = "修改后服务费", orderNum = "9")
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal newAgencyFee;
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    @Excel(name = "修改后物流费", orderNum = "10")
    private BigDecimal newWlTotalFee;
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    @Excel(name = "修改后订单金额", orderNum = "11")
    private BigDecimal newTotalFee;

    public String getCustomerType() {
        switch (customerTypeStr) {
            case "1":
                return "个人";
            case "2":
                return "企业";
            case "3":
                return "合伙人";
            default:
                return "";
        }
    }
}
