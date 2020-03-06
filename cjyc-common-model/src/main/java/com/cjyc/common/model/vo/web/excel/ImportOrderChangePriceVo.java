package com.cjyc.common.model.vo.web.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.cjyc.common.model.serizlizer.BigDecimalSerizlizer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ImportOrderChangePriceVo {
    @Excel(name = "修改人", needMerge = true, orderNum = "1")
    private String createUserName;

    @Excel(name = "订单编号", needMerge = true, orderNum = "2")
    private String no;
    @Excel(name = "客户名称", orderNum = "3")
    private String customerName;
    @Excel(name = "客户类型", orderNum = "4")
    private String customerTypeStr;
    private Integer customerType;
    @Excel(name = "修改时间", orderNum = "5")
    private String createTimeStr;

    @Excel(name = "修改前车辆数", orderNum = "6")
    private Integer oldCarNum;
    @Excel(name = "修改前服务费", orderNum = "7")
    private String oldAgencyFee;
    @Excel(name = "修改前物流费", orderNum = "8")
    private String oldWlTotalFee;
    @Excel(name = "修改前订单金额", orderNum = "9")
    private String oldTotalFee;
    @Excel(name = "修改后车辆数", orderNum = "10")
    private Integer newCarNum;
    @Excel(name = "修改后服务费", orderNum = "11")
    private String newAgencyFee;
    @Excel(name = "修改后物流费", orderNum = "12")
    private String newWlTotalFee;
    @Excel(name = "修改后订单金额", orderNum = "13")
    private String newTotalFee;

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
