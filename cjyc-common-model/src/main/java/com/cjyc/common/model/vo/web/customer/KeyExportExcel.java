package com.cjyc.common.model.vo.web.customer;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.cjyc.common.model.constant.TimePatternConstant;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class KeyExportExcel extends BaseExportExcel {
    private static final long serialVersionUID = 336846592944382829L;
    @Excel(name = "全称" ,orderNum = "9",width = 15)
    private String name;

    @Excel(name = "客户类型" ,orderNum = "10",width = 15)
    private Integer customerNature;

    @Excel(name = "状态" ,orderNum = "11",width = 15)
    private Integer state;

    public String getCustomerNature(){
        if(customerNature != null){
            if(customerNature == 0){
                return "电商";
            }else if(customerNature == 1){
                return "租赁";
            }else if(customerNature == 2){
                return "金融公司";
            }else if(customerNature == 3){
                return "经销商";
            }
        }
        return "";
    }
    public String getState(){
        if(state != null){
            if(state == 0){
                return "待审核";
            }else if(state == 1){
                return "审核中";
            }else if(state == 2){
                return "已审核";
            }else if(state == 3){
                return "审核拒绝";
            }else if(state == 7){
                return "已冻结";
            }
        }
        return "";
    }
}