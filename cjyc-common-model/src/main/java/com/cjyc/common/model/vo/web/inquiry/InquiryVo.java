package com.cjyc.common.model.vo.web.inquiry;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class InquiryVo implements Serializable {

    @ApiModelProperty("询价id")
    private Long id;

    @ApiModelProperty("处理状态 1：未处理  2：已处理")
    private Integer state;

    @ApiModelProperty("是否标红 1：不标红  2：标红")
    private Integer isRed;

    @ApiModelProperty("询价时间")
    private String inquiryTime;

    @ApiModelProperty("运费/元")
    private BigDecimal logisticsFee;

    @ApiModelProperty("上门提车费/元")
    private BigDecimal pickFee;

    @ApiModelProperty("送车费/元")
    private BigDecimal backFee;

    @ApiModelProperty("始发地")
    private String fromCity;

    @ApiModelProperty("目的地")
    private String toCity;

    @ApiModelProperty("客户姓名")
    private String name;

    @ApiModelProperty("客户手机号")
    private String phone;

    @ApiModelProperty("处理时间")
    private String handleTime;

    @ApiModelProperty("处理人")
    private String handlUserName;
}