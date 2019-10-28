package com.cjyc.common.model.dto.web.inquiry;

import com.cjyc.common.model.dto.BasePageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class SelectInquiryDto extends BasePageDto implements Serializable {

    @ApiModelProperty("登陆用户userId")
    private Long userId;

    @ApiModelProperty("询价开始日期")
    private String startDate;

    @ApiModelProperty("询价结束日期")
    private String endDate;

    @ApiModelProperty("始发地")
    private String startCity;

    @ApiModelProperty("目的地")
    private String endCity;

    @ApiModelProperty("处理状态 1：未处理  2：已处理")
    private String state;

    @ApiModelProperty("询价开始时间戳")
    private Long startStamp;

    @ApiModelProperty("询价结束时间戳")
    private Long endStamp;

}