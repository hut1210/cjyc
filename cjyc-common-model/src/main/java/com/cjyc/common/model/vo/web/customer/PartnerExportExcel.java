package com.cjyc.common.model.vo.web.customer;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.cjyc.common.model.constant.TimePatternConstant;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class PartnerExportExcel extends BaseExportExcel {
    private static final long serialVersionUID = -5590379961565825290L;
    @Excel(name = "合伙人名称" ,orderNum = "9",width = 25)
    private String name;

    @Excel(name = "是否一般纳税人" ,orderNum = "10",width = 15)
    private Integer isTaxpayer;

    @Excel(name = "是否可以开票" ,orderNum = "11",width = 15)
    private Integer isInvoice;

    @Excel(name = "结算方式" ,orderNum = "12",width = 15)
    private Integer settleType;

    @Excel(name = "账期/天" ,orderNum = "13",width = 15)
    private Integer settlePeriod;

    @Excel(name = "备注" ,orderNum = "14",width = 60)
    private String description;

    public String getIsTaxpayer(){
        if(isTaxpayer != null){
            if(isTaxpayer == 0){
                return "否";
            }else if(isTaxpayer == 1){
                return "是";
            }
        }
        return "";
    }
    public String getIsInvoice(){
        if(isInvoice != null){
            if(isInvoice == 0){
                return "否";
            }else if(isInvoice == 1){
                return "是";
            }
        }
        return "";
    }
    public String getSettleType(){
        if(settleType != null){
            if(settleType == 0){
                return "时付";
            }else if(settleType == 1){
                return "账期";
            }
        }
        return "";
    }

}