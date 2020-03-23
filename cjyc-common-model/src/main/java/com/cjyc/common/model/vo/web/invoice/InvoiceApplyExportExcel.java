package com.cjyc.common.model.vo.web.invoice;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;

/**
 * @Description 发票记录导出实体
 * @Author LiuXingXiang
 * @Date 2019/11/6 11:11
 **/
@Data
public class InvoiceApplyExportExcel implements Serializable {
    private static final long serialVersionUID = -5393390678375641705L;

    @Excel(name = "申请人" ,orderNum = "0",width = 15)
    private String customerName;

    private Long applyTime;
    @Excel(name = "申请时间" ,orderNum = "1",width = 15)
    private String applyTimeStr;

    @Excel(name = "开票金额" ,orderNum = "2",width = 15,type = 10)
    private BigDecimal amount;

    @Excel(name = "发票号" ,orderNum = "3",width = 15)
    private String invoiceNo;

    private Long invoiceTime;
    @Excel(name = "开票时间" ,orderNum = "4",width = 15)
    private String invoiceTimeStr;

    @Excel(name = "开票人" ,orderNum = "5",width = 15)
    private String operationName;

    @Excel(name = "发票状态" ,orderNum = "6",width = 15,replace = {"申请中_1","已开票_2"})
    private Integer state;

    public String getApplyTimeStr() {
        if (applyTime != null) {
            LocalDateTime localDateTime = LocalDateTimeUtil.convertLongToLDT(applyTime);
            return LocalDateTimeUtil.formatLDT(localDateTime,"yyyy/MM/dd");
        }
        return "";
    }

    public String getInvoiceTimeStr() {
        if (invoiceTime != null) {
            LocalDateTime localDateTime = LocalDateTimeUtil.convertLongToLDT(invoiceTime);
            return LocalDateTimeUtil.formatLDT(localDateTime,"yyyy/MM/dd");
        }
        return "";
    }

    public String getAmount() {
        if (amount == null) {
            return "0";
        }
        DecimalFormat df =new DecimalFormat("0.00");
        BigDecimal value = amount.divide(BigDecimal.valueOf(100));
        return amount.equals(BigDecimal.ZERO) ? "0.00" : df.format(value);
    }
}
