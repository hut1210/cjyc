package com.yqzl.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author:Hut
 * @Date:2019/11/29 14:58
 */
@Data
public class Head {

    @ApiModelProperty("交易码")
    @NotNull
    private String trCode;

    @ApiModelProperty("企业代码")
    @NotNull
    private String corpNo;

    @ApiModelProperty("企业用户号")
    @NotNull
    private String userNo;

    @ApiModelProperty("发起方序号")
    private String reqNo;

    @ApiModelProperty("交易日期 YYYYMMdd")
    @NotNull
    private String trAcdt;

    @ApiModelProperty("时间 hhmmss")
    @NotNull
    private String trTime;

    @ApiModelProperty("原子交易数")
    @NotNull
    private String atomTrCount;

    @ApiModelProperty("渠道标志")
    @NotNull
    private String channel;

    @ApiModelProperty("保留字段")
    @NotNull
    private String reserved;

    public Head(String corpNo, String userNo, String trCode, String reqNo, String trAcdt, String trTime, String atomTrCount, String channel, String reserved) {
        this.corpNo = corpNo;
        this.userNo = userNo;
        this.trCode = trCode;
        this.reqNo = reqNo;
        this.trAcdt = trAcdt;
        this.trTime = trTime;
        this.atomTrCount = atomTrCount;
        this.channel = channel;
        this.reserved = reserved;
    }

    public String getHead(Head head) {
        return "<head><tr_code>" + head.getTrCode() + "</tr_code>" +
                "<corp_no>" + head.getCorpNo() + "</corp_no>" +
                "<user_no>" + head.getUserNo() + "</user_no>" +
                "<req_no>" + head.getReqNo() + "</req_no>" +
                "<tr_acdt>" + head.getTrAcdt() + "</tr_acdt>" +
                "<tr_time>" + head.getTrTime() + "</tr_time>" +
                "<atom_tr_count>" + head.getAtomTrCount() + "</atom_tr_count>" +
                "<channel >0</channel>" +
                "<reserved>" + head.getReserved() + "</reserved></head>";
    }
}
