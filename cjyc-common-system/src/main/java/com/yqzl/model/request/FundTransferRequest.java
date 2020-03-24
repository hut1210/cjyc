package com.yqzl.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 银行转账请求对象
 *
 * @author RenPL 2020-3-15
 */
@Data
public class FundTransferRequest {

    @ApiModelProperty(value = "账务中心支持的银行产品编码:  COMM_BANK:交通银行", required = true)
    private String bankProCode;

    /**
     * 请求体head信息
     */
    @ApiModelProperty(value = "交易码", required = true)
    private String trCode;

    @ApiModelProperty(value = "企业代码", hidden = true)
    private String corpNo;

    @ApiModelProperty(value = "企业用户号", hidden = true)
    private String userNo;

    @ApiModelProperty("发起方序号")
    private String reqNo;

    @ApiModelProperty("交易日期 YYYYMMdd")
    private String trAcdt;

    @ApiModelProperty("时间 hhmmss")
    private String trTime;

    @ApiModelProperty("原子交易数")
    private String atomTrCount;

    @ApiModelProperty("渠道标志")
    private String channel;

    @ApiModelProperty("保留字段")
    private String reserved;

    /**
     * 请求体body信息
     */
    @ApiModelProperty(value = "付款人帐号", required = true)
    private String payAcno;

    @ApiModelProperty(value = "付款人户名", required = true)
    private String payAcname;

    @ApiModelProperty(value = "收款方行名", required = true)
    private String rcvBankName;

    @ApiModelProperty(value = "收款人帐号", required = true)
    private String rcvAcno;

    @ApiModelProperty(value = "收款人户名", required = true)
    private String rcvAcname;

    @ApiModelProperty("收款方交换号")
    private String rcvExgCode;

    @ApiModelProperty("收款方联行号")
    private String rcvBankNo;

    @ApiModelProperty(value = "币种", required = true)
    private String curCode;

    @ApiModelProperty(value = "金额", required = true)
    private BigDecimal amt;

    @ApiModelProperty(value = "企业凭证编号", required = true)
    private String certNo;

    @ApiModelProperty("附言")
    private String summary;

    @ApiModelProperty(value = "银行标志", required = true)
    private String bankFlag;

    @ApiModelProperty("同城异地标志")
    private String areaFlag;

}
