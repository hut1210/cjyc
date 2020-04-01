package com.cjyc.common.model.dto.web.finance;

import com.cjyc.common.model.enums.InvoiceTypeEnum;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>待开票列表查询返回参数实体类Dto</p>
 *
 * @Author:RenPL
 * @Date:2020/3/20 15:57
 */
@Data
public class ReceiveSettlementDto {

    @ApiModelProperty(value = "结算流水号")
    private String serialNumber;

    @ApiModelProperty(value = "应收运费(元)")
    private BigDecimal totalReceivableFee;

    @ApiModelProperty(value = "开票金额(元)")
    private BigDecimal totalInvoiceFee;

    @ApiModelProperty(value = "差额(元)")
    private BigDecimal differenceFee;

    @ApiModelProperty(value = "发票类型 1-普通(个人) ，2-增值普票(企业) ，3-增值专用发票'")
    private Integer invoiceType;

    @ApiModelProperty(value = "发票类型名称")
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

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "纳税人识别号")
    private String taxCode;

    @ApiModelProperty(value = "公司电话")
    private String phone;

    @ApiModelProperty(value = "公司地址")
    private String invoiceAddress;

    @ApiModelProperty(value = "开户银行名称")
    private String bankName;

    @ApiModelProperty(value = "开户行账号")
    private String bankAccount;

    @ApiModelProperty(value = "收件人")
    private String pickupPerson;

    @ApiModelProperty(value = "收件人电话")
    private String pickupPhone;

    @ApiModelProperty(value = "收件地址")
    private String pickupAddress;

    @ApiModelProperty(value = "申请时间")
    private Long applyTime;

    @ApiModelProperty(value = "申请时间显示格式")
    private String applyTimeStr;

    public String getApplyTimeStr() {
        Long applyTimeDate = getApplyTime();
        if (null == applyTimeDate || applyTimeDate <= 0L) {
            return "";
        }
        return LocalDateTimeUtil.formatLDT(LocalDateTimeUtil.convertLongToLDT(applyTimeDate), "yyyy-MM-dd");
    }

    @ApiModelProperty(value = "申请人")
    private String applicantName;

    @ApiModelProperty(value = "确认时间")
    private Long confirmTime;

    @ApiModelProperty(value = "确认时间显示格式")
    private String confirmTimeStr;

    public String getConfirmTimeStr() {
        Long confirmTimeDate = getConfirmTime();
        if (null == confirmTimeDate || confirmTimeDate <= 0L) {
            return "";
        }
        return LocalDateTimeUtil.formatLDT(LocalDateTimeUtil.convertLongToLDT(confirmTimeDate), "yyyy-MM-dd");
    }

    @ApiModelProperty(value = "确认人")
    private String confirmName;

    @ApiModelProperty(value = "核销时间")
    private Long verificationTime;

    @ApiModelProperty(value = "核销时间显示格式")
    private String verificationTimeStr;

    public String getVerificationTimeStr() {
        Long verificationTimeDate = getVerificationTime();
        if (null == verificationTimeDate || verificationTimeDate <= 0L) {
            return "";
        }
        return LocalDateTimeUtil.formatLDT(LocalDateTimeUtil.convertLongToLDT(verificationTimeDate), "yyyy-MM-dd");
    }

    @ApiModelProperty(value = "核销人")
    private String verificationName;

    @ApiModelProperty(value = "发票号")
    private String invoiceNo;

}
