package com.cjyc.common.model.vo.web.finance;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: Hut
 * @Date: 2020/01/06 10:09
 * 应付-申请结算
 **/
@Data
public class SettlementVo {

    @ApiModelProperty(value = "运单单号")
    private String no;

    @ApiModelProperty(value = "结算流水号")
    @Excel(name = "结算流水号" ,orderNum = "0")
    private String serialNumber;

    @ApiModelProperty(value = "应付运费")
    @Excel(name = "应付总运费" ,orderNum = "1")
    private BigDecimal freightFee;

    @ApiModelProperty(value = "状态")
    private String state;

    @ApiModelProperty(value = "发票号")
    @Excel(name = "发票号" ,orderNum = "8")
    private String invoiceNo;

    @ApiModelProperty(value = "申请结算时间")
    private Long applyTime;

    @Excel(name = "申请结算时间" ,orderNum = "3")
    private String applyTimeStr;

    public String getApplyTimeStr() {
        Long date = getApplyTime();
        if (null == date || date <= 0L) {
            return "";
        }
        return LocalDateTimeUtil.formatLDT(LocalDateTimeUtil.convertLongToLDT(date), "yyyy-MM-dd");
    }

    private Long applicantId;

    @ApiModelProperty(value = "申请人")
    @Excel(name = "申请人" ,orderNum = "4")
    private String applicant;

    @ApiModelProperty(value = "确认开票时间")
    private Long confirmTime;

    @Excel(name = "确认开票时间" ,orderNum = "5")
    private String confirmTimeStr;

    public String getConfirmTimeStr() {
        Long date = getConfirmTime();
        if (null == date || date <= 0L) {
            return "";
        }
        return LocalDateTimeUtil.formatLDT(LocalDateTimeUtil.convertLongToLDT(date), "yyyy-MM-dd");
    }

    @ApiModelProperty(value = "确认人Id")
    private Long confirmId;

    @ApiModelProperty(value = "确认人")
    @Excel(name = "运单单号" ,orderNum = "6")
    private String confirmMan;

    @ApiModelProperty(value = "核销时间")
    private Long writeOffTime;

    @Excel(name = "确认开票时间" ,orderNum = "7")
    private String writeOffTimeStr;

    public String getWriteOffTimeStr() {
        Long date = getWriteOffTime();
        if (null == date || date <= 0L) {
            return "";
        }
        return LocalDateTimeUtil.formatLDT(LocalDateTimeUtil.convertLongToLDT(date), "yyyy-MM-dd");
    }

    @ApiModelProperty(value = "核销人Id")
    private Long writeOffId;

    @ApiModelProperty(value = "核销人")
    @Excel(name = "核销人" ,orderNum = "9")
    private String writeOffMan;

    @ApiModelProperty(value = "实付总运费")
    @Excel(name = "实付总运费" ,orderNum = "2")
    private BigDecimal totalFreightPay;
}
