package com.yqzl.model;

import com.yqzl.config.YqzlProperty;
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
    private String tr_code;

    @ApiModelProperty("企业代码")
    @NotNull
    private String corp_no;

    @ApiModelProperty("企业用户号")
    @NotNull
    private String user_no;

    @ApiModelProperty("发起方序号")
    private String req_no;

    @ApiModelProperty("交易日期 YYYYMMdd")
    @NotNull
    private String tr_acdt;

    @ApiModelProperty("时间 hhmmss")
    @NotNull
    private String tr_time;

    @ApiModelProperty("原子交易数")
    @NotNull
    private String atom_tr_count;

    @ApiModelProperty("渠道标志")
    @NotNull
    private String channel;

    @ApiModelProperty("保留字段")
    @NotNull
    private String reserved;

    public Head(@NotNull String tr_code, String req_no, @NotNull String tr_acdt, @NotNull String tr_time, @NotNull String atom_tr_count, @NotNull String channel, @NotNull String reserved) {
        this.tr_code = tr_code;
        this.req_no = req_no;
        this.tr_acdt = tr_acdt;
        this.tr_time = tr_time;
        this.atom_tr_count = atom_tr_count;
        this.channel = channel;
        this.reserved = reserved;
    }

    public String getHead(Head head) {
        return "<head><tr_code>"+head.getTr_code()+"</tr_code>" +
                "<corp_no>"+YqzlProperty.corp_no +"</corp_no>" +
                "<user_no>"+YqzlProperty.user_no+"</user_no>"+
                "<req_no>"+head.getReq_no()+"</req_no>" +
                "<tr_acdt>"+head.getTr_acdt()+"</tr_acdt>" +
                "<tr_time>"+head.getTr_time()+"</tr_time>" +
                "<atom_tr_count>"+head.getAtom_tr_count()+"</atom_tr_count>" +
                "<channel >0</channel>" +
                "<reserved>"+head.getReserved()+"</reserved></head>";
    }
}
