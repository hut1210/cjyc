package com.cjyc.common.model.vo.web.finance;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author: Hut
 * @Date: 2020/04/13 17:36
 **/
@Data
public class ExportPayablePaidVo implements Serializable {

    @ApiModelProperty(value = "运单单号")
    private String no;

    @ApiModelProperty(value = "结算流水号")
    @Excel(name = "结算流水号" ,orderNum = "0")
    private String serialNumber;

    @ApiModelProperty(value = "应付运费")
    @Excel(name = "应付总运费" ,orderNum = "1",type = 10)
    private BigDecimal freightFee;

    @ApiModelProperty(value = "状态")
    private String state;

    @ApiModelProperty(value = "实付总运费")
    @Excel(name = "实付总运费" ,orderNum = "2",type = 10)
    private BigDecimal totalFreightPay;

    @ApiModelProperty(value = "差额")
    @Excel(name = "差额" ,orderNum = "3",type = 10)
    private BigDecimal difference;

    @ApiModelProperty(value = "发票号")
    @Excel(name = "发票号" ,orderNum = "4")
    private String invoiceNo;

    @ApiModelProperty(value = "申请结算时间")
    private Long applyTime;

    @Excel(name = "申请结算时间" ,orderNum = "5")
    private String applyTimeStr;

    public String getApplyTimeStr() {
        Long date = getApplyTime();
        if (null == date || date <= 0L) {
            return "";
        }
        return LocalDateTimeUtil.formatLDT(LocalDateTimeUtil.convertLongToLDT(date), "yyyy-MM-dd HH:mm:ss");
    }

    private Long applicantId;

    @ApiModelProperty(value = "申请人")
    @Excel(name = "申请人" ,orderNum = "6")
    private String applicant;

    @ApiModelProperty(value = "确认收票时间")
    private Long confirmTime;

    @Excel(name = "确认收票时间" ,orderNum = "7")
    private String confirmTimeStr;

    public String getConfirmTimeStr() {
        Long date = getConfirmTime();
        if (null == date || date <= 0L) {
            return "";
        }
        return LocalDateTimeUtil.formatLDT(LocalDateTimeUtil.convertLongToLDT(date), "yyyy-MM-dd HH:mm:ss");
    }

    @ApiModelProperty(value = "确认人Id")
    private Long confirmId;

    @ApiModelProperty(value = "确认人")
    @Excel(name = "确认人" ,orderNum = "8")
    private String confirmMan;

    @ApiModelProperty(value = "核销时间")
    private Long writeOffTime;

    @Excel(name = "核销时间" ,orderNum = "9")
    private String writeOffTimeStr;

    public String getWriteOffTimeStr() {
        Long date = getWriteOffTime();
        if (null == date || date <= 0L) {
            return "";
        }
        return LocalDateTimeUtil.formatLDT(LocalDateTimeUtil.convertLongToLDT(date), "yyyy-MM-dd HH:mm:ss");
    }

    @ApiModelProperty(value = "核销人Id")
    private Long writeOffId;

    @ApiModelProperty(value = "核销人")
    @Excel(name = "核销人" ,orderNum = "10")
    private String writeOffMan;


}
