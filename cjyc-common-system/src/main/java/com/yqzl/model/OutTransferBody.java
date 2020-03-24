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
    private String pay_acno;

    @ApiModelProperty("付款人户名")
    private String pay_acname;

    @ApiModelProperty("收款方行名")
    private String rcv_bank_name;

    @ApiModelProperty("收款人帐号")
    private String rcv_acno;

    @ApiModelProperty("收款人户名")
    private String rcv_acname;

    @ApiModelProperty("收款方交换号")
    private String rcv_exg_code;

    @ApiModelProperty("收款方联行号")
    private String rcv_bank_no;

    @ApiModelProperty("币种")
    private String cur_code;

    @ApiModelProperty("金额")
    private BigDecimal amt;

    @ApiModelProperty("企业凭证编号")
    private String cert_no;

    @ApiModelProperty("附言")
    private String summary;

    @ApiModelProperty("银行标志")
    private String bank_flag;

    @ApiModelProperty("同城异地标志")
    private String area_flag;

    public OutTransferBody(String pay_acno, String pay_acname, String rcv_bank_name, String rcv_acno, String rcv_acname, String rcv_exg_code, String rcv_bank_no, String cur_code, BigDecimal amt, String cert_no, String summary, String bank_flag, String area_flag) {
        this.pay_acno = pay_acno;
        this.pay_acname = pay_acname;
        this.rcv_bank_name = rcv_bank_name;
        this.rcv_acno = rcv_acno;
        this.rcv_acname = rcv_acname;
        this.rcv_exg_code = rcv_exg_code;
        this.rcv_bank_no = rcv_bank_no;
        this.cur_code = cur_code;
        this.amt = amt;
        this.cert_no = cert_no;
        this.summary = summary;
        this.bank_flag = bank_flag;
        this.area_flag = area_flag;
    }

    public String getOutTransferBody(OutTransferBody outTransferBody){

        return "<body><pay_acno>"+ outTransferBody.getPay_acno()+"</pay_acno>" +
                "<pay_acname>"+ outTransferBody.getPay_acname()+"</pay_acname>"+
                "<rcv_bank_name>"+ outTransferBody.getRcv_bank_name()+"</rcv_bank_name>" +
                "<rcv_acno>"+ outTransferBody.getRcv_acno()+"</rcv_acno>" +
                "<rcv_acname>"+ outTransferBody.getRcv_acname()+"</rcv_acname>"+
                "<cur_code>CNY</cur_code>" +
                "<amt>"+ outTransferBody.getAmt()+"</amt>" +
                "<cert_no>"+ outTransferBody.getCert_no()+"</cert_no>" +
                "<bank_flag>"+ outTransferBody.getBank_flag()+"</bank_flag></body>";
    }
}
