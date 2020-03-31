package com.cjyc.common.model.dto.web.finance;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.cjyc.common.model.enums.InvoiceTypeEnum;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>待回款，已收款列表下载实体类Dto</p>
 *
 * @Author:RenPL
 * @Date:2020/3/20 15:57
 */
@Data
public class ReceiveSettlementExcelItemDto {

    @Excel(name = "结算流水号", orderNum = "0")
    private String serialNumber;

    @Excel(name = "应收运费(元)", orderNum = "1", type = 10)
    private BigDecimal totalReceivableFee;

    @Excel(name = "开票金额(元)", orderNum = "2", type = 10)
    private BigDecimal totalInvoiceFee;

    @Excel(name = "差额(元)", orderNum = "3", type = 10)
    private BigDecimal differenceFee;

    @Excel(name = "客户名称", orderNum = "4")
    private String customerName;

    @Excel(name = "纳税人识别号", orderNum = "5")
    private String taxCode;

    @Excel(name = "公司电话", orderNum = "6")
    private String phone;

    @Excel(name = "公司地址", orderNum = "7")
    private String invoiceAddress;

    @Excel(name = "开户银行名称", orderNum = "8")
    private String bankName;

    @Excel(name = "开户行账号", orderNum = "9")
    private String bankAccount;

    private Integer invoiceType;

    @Excel(name = "发票类型", orderNum = "10")
    private String invoiceTypeStr;
    public String getInvoiceTypeStr() {
        if (invoiceType == null) {
            return "";
        }
        if (InvoiceTypeEnum.PERSONAL_INVOICE.code == invoiceType) {
            return "个人普票";
        } else if (InvoiceTypeEnum.COMPANY_INVOICE.code == invoiceType) {
            return "公司普票";
        } else if (InvoiceTypeEnum.SPECIAL_INVOICE.code == invoiceType) {
            return "公司专票";
        }
        return "未知发票类型";
    }

    @Excel(name = "收件人", orderNum = "11")
    private String pickupPerson;

    @Excel(name = "收件人电话", orderNum = "12")
    private String pickupPhone;

    @Excel(name = "收件地址", orderNum = "13")
    private String pickupAddress;

    @Excel(name = "发票号", orderNum = "14")
    private String invoiceNo;

    private Long applyTime;

    @Excel(name = "申请结算时间", orderNum = "15")
    private String applyTimeStr;
    public String getApplyTimeStr() {
        Long applyTimeDate = getApplyTime();
        if (null == applyTimeDate || applyTimeDate <= 0L) {
            return "";
        }
        return LocalDateTimeUtil.formatLDT(LocalDateTimeUtil.convertLongToLDT(applyTimeDate), "yyyy-MM-dd");
    }

    @Excel(name = "申请人", orderNum = "16")
    private String applicantName;

    private Long confirmTime;

    @Excel(name = "确认开票时间", orderNum = "17")
    private String confirmTimeStr;
    public String getConfirmTimeStr() {
        Long confirmTimeDate = getConfirmTime();
        if (null == confirmTimeDate || confirmTimeDate <= 0L) {
            return "";
        }
        return LocalDateTimeUtil.formatLDT(LocalDateTimeUtil.convertLongToLDT(confirmTimeDate), "yyyy-MM-dd");
    }

    @Excel(name = "确认人", orderNum = "18")
    private String confirmName;

    private Long verificationTime;

    @Excel(name = "核销时间", orderNum = "19")
    private String verificationTimeStr;
    public String getVerificationTimeStr() {
        Long verificationTimeDate = getVerificationTime();
        if (null == verificationTimeDate || verificationTimeDate <= 0L) {
            return "";
        }
        return LocalDateTimeUtil.formatLDT(LocalDateTimeUtil.convertLongToLDT(verificationTimeDate), "yyyy-MM-dd");
    }

    @Excel(name = "核销人", orderNum = "20")
    private String verificationName;
}
