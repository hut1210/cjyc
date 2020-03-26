package com.cjyc.common.model.dto.web.finance;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.StringUtils;

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
    @Excel(name = "结算流水号", orderNum = "0")
    private String serialNumber;

    @ApiModelProperty(value = "应收运费(元)")
    @Excel(name = "应收运费(元)", orderNum = "1", type = 10)
    private BigDecimal totalReceivableFee;

    @ApiModelProperty(value = "开票金额(元)")
    @Excel(name = "开票金额(元)", orderNum = "2", type = 10)
    private BigDecimal totalInvoiceFee;

    @ApiModelProperty(value = "差额(元)")
    @Excel(name = "差额(元)", orderNum = "3", type = 10)
    private BigDecimal differenceFee;

    @ApiModelProperty(value = "发票类型 1-普通(个人) ，2-增值普票(企业) ，3-增值专用发票'")
    private String invoiceType;

    @Excel(name = "发票类型", orderNum = "4")
    private String invoiceTypeStr;

    public String getInvoiceTypeStr() {
        if (StringUtils.isEmpty(invoiceType)) {
            return "";
        }
        if ("1".equals(invoiceType)) {
            return "个人普票";
        } else if ("2".equals(invoiceType)) {
            return "公司普票";
        } else if ("3".equals(invoiceType)) {
            return "公司专票";
        }
        return "未知发票类型";
    }

    @ApiModelProperty(value = "客户名称")
    @Excel(name = "客户名称", orderNum = "5")
    private String customerName;

    @ApiModelProperty(value = "纳税人识别号")
    @Excel(name = "纳税人识别号", orderNum = "6")
    private String taxCode;

    @ApiModelProperty(value = "公司电话")
    @Excel(name = "公司电话", orderNum = "7")
    private String phone;

    @ApiModelProperty(value = "公司地址")
    @Excel(name = "公司地址", orderNum = "8")
    private String invoiceAddress;

    @ApiModelProperty(value = "开户银行名称")
    @Excel(name = "开户银行名称", orderNum = "9")
    private String bankName;

    @ApiModelProperty(value = "开户行账号")
    @Excel(name = "开户行账号", orderNum = "10")
    private String bankAccount;

    @ApiModelProperty(value = "收件人")
    @Excel(name = "收件人", orderNum = "11")
    private String pickupPerson;

    @ApiModelProperty(value = "收件人电话")
    @Excel(name = "收件人电话", orderNum = "12")
    private String pickupPhone;

    @ApiModelProperty(value = "收件地址")
    @Excel(name = "收件地址", orderNum = "13")
    private String pickupAddress;

    @ApiModelProperty(value = "申请时间")
    private Long applyTime;

    @Excel(name = "申请时间", orderNum = "14")
    private String applyTimeStr;

    public String getApplyTimeStr() {
        Long applyTimeDate = getApplyTime();
        if (null == applyTimeDate || applyTimeDate <= 0L) {
            return "";
        }
        return LocalDateTimeUtil.formatLDT(LocalDateTimeUtil.convertLongToLDT(applyTimeDate), "yyyy-MM-dd");
    }

    @ApiModelProperty(value = "申请人")
    @Excel(name = "申请人", orderNum = "15")
    private String applicantName;

    @ApiModelProperty(value = "确认时间")
    private Long confirmTime;

    @Excel(name = "确认时间", orderNum = "16")
    private String confirmTimeStr;

    public String getConfirmTimeStr() {
        Long confirmTimeDate = getConfirmTime();
        if (null == confirmTimeDate || confirmTimeDate <= 0L) {
            return "";
        }
        return LocalDateTimeUtil.formatLDT(LocalDateTimeUtil.convertLongToLDT(confirmTimeDate), "yyyy-MM-dd");
    }

    @ApiModelProperty(value = "确认人")
    @Excel(name = "确认人", orderNum = "17")
    private String confirmName;

    @ApiModelProperty(value = "核销时间")
    private Long verificationTime;

    @Excel(name = "核销时间", orderNum = "18")
    private String verificationTimeStr;

    public String getVerificationTimeStr() {
        Long verificationTimeDate = getVerificationTime();
        if (null == verificationTimeDate || verificationTimeDate <= 0L) {
            return "";
        }
        return LocalDateTimeUtil.formatLDT(LocalDateTimeUtil.convertLongToLDT(verificationTimeDate), "yyyy-MM-dd");
    }

    @ApiModelProperty(value = "核销人")
    @Excel(name = "核销人", orderNum = "19")
    private String verificationName;

    @ApiModelProperty(value = "发票号")
    private String invoiceNo;

}
