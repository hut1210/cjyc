package com.yqzl.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author:Hut
 * @Date:2019/11/29 15:59
 */
@Data
public class OutTransferBody {

    @ApiModelProperty("付款人帐号")
    private String payAcno;

    @ApiModelProperty("付款人户名")
    private String payAcname;

    @ApiModelProperty("收款方行名")
    private String rcvBankName;

    @ApiModelProperty("收款人帐号")
    private String rcvAcno;

    @ApiModelProperty("收款人户名")
    private String rcvAcname;

    @ApiModelProperty("收款方交换号")
    private String rcvExgCode;

    @ApiModelProperty("收款方联行号")
    private String rcvBankNo;

    @ApiModelProperty("币种")
    private String curCode;

    @ApiModelProperty("金额")
    private BigDecimal amt;

    @ApiModelProperty("企业凭证编号")
    private String certNo;

    @ApiModelProperty("附言")
    private String summary;

    @ApiModelProperty("银行标志")
    private String bankFlag;

    @ApiModelProperty("同城异地标志")
    private String areaFlag;

    public OutTransferBody(String payAcno, String payAcname, String rcvBankName, String rcvAcno, String rcvAcname, String rcvExgCode, String rcvBankNo, String curCode, BigDecimal amt, String certNo, String summary, String bankFlag, String areaFlag) {
        this.payAcno = payAcno;
        this.payAcname = payAcname;
        this.rcvBankName = rcvBankName;
        this.rcvAcno = rcvAcno;
        this.rcvAcname = rcvAcname;
        this.rcvExgCode = rcvExgCode;
        this.rcvBankNo = rcvBankNo;
        this.curCode = curCode;
        this.amt = amt;
        this.certNo = certNo;
        this.summary = summary;
        this.bankFlag = bankFlag;
        this.areaFlag = areaFlag;
    }

    public String getOutTransferBody(OutTransferBody outTransferBody) {

        return "<body><pay_acno>" + outTransferBody.getPayAcno() + "</pay_acno>" +
                "<pay_acname>" + outTransferBody.getPayAcname() + "</pay_acname>" +
                "<rcv_bank_name>" + outTransferBody.getRcvBankName() + "</rcv_bank_name>" +
                "<rcv_acno>" + outTransferBody.getRcvAcno() + "</rcv_acno>" +
                "<rcv_acname>" + outTransferBody.getRcvAcname() + "</rcv_acname>" +
                "<cur_code>CNY</cur_code>" +
                "<amt>" + outTransferBody.getAmt() + "</amt>" +
                "<cert_no>" + outTransferBody.getCertNo() + "</cert_no>" +
                "<bank_flag>" + outTransferBody.getBankFlag() + "</bank_flag></body>";
    }
}
