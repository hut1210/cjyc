package com.cjyc.common.model.dto.customer;

import com.cjyc.common.model.dto.BasePageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class OrderConditionDto extends BasePageDto implements Serializable {

    @ApiModelProperty("关键字")
    private String key;

    @ApiModelProperty("排序 0:默认排序 1：时间升序 2：时间降序")
    private String sort;

    @ApiModelProperty("业务中心编码")
    private String storeId;

    @ApiModelProperty("品牌")
    private String brand;

    @ApiModelProperty("车型")
    private String model;

    @ApiModelProperty("下单开始时间")
    private String startDate;

    @ApiModelProperty("下单结束时间")
    private String endDate;

    @ApiModelProperty("始发地编码")
    private String startCityCode;

    @ApiModelProperty("目的地编码")
    private String endCityCode;
}