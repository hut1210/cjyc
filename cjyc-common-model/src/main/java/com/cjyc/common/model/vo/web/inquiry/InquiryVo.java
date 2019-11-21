package com.cjyc.common.model.vo.web.inquiry;

import com.cjyc.common.model.util.BigDecimalSerizlizer;
import com.cjyc.common.model.util.DataLongSerizlizer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class InquiryVo implements Serializable {

    private static final long serialVersionUID = -6669376069175092381L;
    @ApiModelProperty("询价id")
    private Long id;

    @ApiModelProperty("处理状态 1：未处理  2：已处理")
    private Integer state;

    @ApiModelProperty("是否标红 1：不标红  2：标红")
    private Integer isRed;

    @ApiModelProperty("询价时间")
    @JsonSerialize(using = DataLongSerizlizer.class)
    private Long inquiryTime;

    @ApiModelProperty("运费/元")
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal logisticsFee;

    @ApiModelProperty("始发地")
    private String fromCity;

    @ApiModelProperty("目的地")
    private String toCity;

    @ApiModelProperty("客户姓名")
    private String name;

    @ApiModelProperty("客户手机号")
    private String phone;

    @ApiModelProperty("处理时间")
    @JsonSerialize(using = DataLongSerizlizer.class)
    private Long handleTime;

    @ApiModelProperty("处理人")
    private String handlUserName;

    @ApiModelProperty("回访工单")
    private String jobContent;
}