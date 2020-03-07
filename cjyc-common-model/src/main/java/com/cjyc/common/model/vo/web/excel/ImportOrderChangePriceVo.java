package com.cjyc.common.model.vo.web.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.cjyc.common.model.serizlizer.BigDecimalSerizlizer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ImportOrderChangePriceVo {
    @Excel(name = "订单编号", needMerge = true,  width = 15,orderNum = "0")
    private String no;
    @Excel(name = "修改人", orderNum = "1")
    private String createUserName;
    @Excel(name = "订单创建人", width = 15, orderNum = "2")
    private String orderCreateUserName;

    @Excel(name = "客户名称", width = 15, orderNum = "3")
    private String customerName;
    @Excel(name = "客户类型", orderNum = "4")
    private String customerTypeStr;
    @Excel(name = "修改时间", width = 15, orderNum = "5")
    private String createTimeStr;

    @Excel(name = "旧状态", width = 18, orderNum = "11")
    private String oldState;
    @Excel(name = "旧车辆数", orderNum = "12")
    private Integer oldCarNum;
    @Excel(name = "旧服务费", orderNum = "13")
    private String oldAgencyFee;
    @Excel(name = "旧物流费", orderNum = "14")
    private String oldWlTotalFee;
    @Excel(name = "旧订单金额", orderNum = "15")
    private String oldTotalFee;
    @Excel(name = "新状态", width = 18, orderNum = "21")
    private String newState;
    @Excel(name = "新车辆数", orderNum = "22")
    private Integer newCarNum;
    @Excel(name = "新服务费", orderNum = "23")
    private String newAgencyFee;
    @Excel(name = "新物流费", orderNum = "24")
    private String newWlTotalFee;
    @Excel(name = "新订单金额", orderNum = "25")
    private String newTotalFee;

}
