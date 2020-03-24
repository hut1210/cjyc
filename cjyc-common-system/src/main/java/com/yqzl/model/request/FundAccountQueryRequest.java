package com.yqzl.model.request;

import io.swagger.annotations.ApiModelProperty;

public class FundAccountQueryRequest {

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

}
