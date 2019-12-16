package com.cjyc.common.model.vo.web.inquiry;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.cjyc.common.model.constant.TimePatternConstant;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class InquiryExportExcel implements Serializable {
    private static final long serialVersionUID = 1591824326342287228L;
    @Excel(name = "处理状态" ,orderNum = "0",width = 25)
    private Integer state;
    @Excel(name = "询价时间" ,orderNum = "1",width = 25)
    private Long inquiryTime;
    @Excel(name = "运费/元" ,orderNum = "2",width = 25)
    private BigDecimal logisticsFee;
    @Excel(name = "始发地" ,orderNum = "3",width = 25)
    private String fromCity;
    @Excel(name = "目的地" ,orderNum = "4",width = 25)
    private String toCity;
    @Excel(name = "客户姓名" ,orderNum = "5",width = 25)
    private String name;
    @Excel(name = "客户手机号" ,orderNum = "6",width = 25)
    private String phone;
    @Excel(name = "处理时间" ,orderNum = "7",width = 25)
    private Long handleTime;
    @Excel(name = "处理人" ,orderNum = "8",width = 25)
    private String handlUserName;
    @Excel(name = "回访工单" ,orderNum = "9",width = 25)
    private String jobContent;
    public String getState(){
        if(state != null){
            if(state == 1){
                return "未处理";
            }else if(state == 2){
                return "已处理";
            }
        }
        return "";
    }
    public String getInquiryTime(){
        if(inquiryTime != null){
            return LocalDateTimeUtil.formatLDT(LocalDateTimeUtil.convertLongToLDT(inquiryTime), TimePatternConstant.COMPLEX_TIME_FORMAT);
        }
        return "";
    }
    public String getHandleTime(){
        if(handleTime != null){
            return LocalDateTimeUtil.formatLDT(LocalDateTimeUtil.convertLongToLDT(handleTime), TimePatternConstant.COMPLEX_TIME_FORMAT);
        }
        return "";
    }
    public BigDecimal getLogisticsFee(){
        return logisticsFee == null ? BigDecimal.ZERO:logisticsFee.divide(new BigDecimal(100));
    }

}