package com.yqzl.model.response;

import lombok.Data;

/**
 * 交行转账查询数据封装对象
 *
 * @author RenPL 2020-3-15
 */
@Data
public class FundTransferQueryResponse {

    public FundTransferQueryResponse(String oglSerialNo, Integer stat, String errMsg, String payAcno, String payBankNo, String rcvAcno, String rcvBankNo, Double amt, Long certNo) {
        this.oglSerialNo = oglSerialNo;
        this.stat = stat;
        this.errMsg = errMsg;
        this.payAcno = payAcno;
        this.payBankNo = payBankNo;
        this.rcvAcno = rcvAcno;
        this.rcvBankNo = rcvBankNo;
        this.amt = amt;
        this.certNo = certNo;
    }

    /**
     * 原流水号
     */
    private String oglSerialNo;

    /**
     * 状态
     */
    private Integer stat;

    /**
     * 错误信息
     */
    private String errMsg;

    /**
     * 付款人账号
     */
    private String payAcno;

    /**
     * 付款行号
     */
    private String payBankNo;

    /**
     * 收款人账号
     */
    private String rcvAcno;

    /**
     * 收款方行号
     */
    private String rcvBankNo;

    /**
     * 金额
     */
    private Double amt;

    /**
     * 企业凭证编号
     */
    private Long certNo;
}
