package com.cjyc.common.model.dto.salesman.mine;

import com.cjyc.common.model.dto.salesman.PageSalesDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class StockCarDto extends PageSalesDto {
    @ApiModelProperty(value = "vin码")
    private String vin;
    @ApiModelProperty(value = "起始地")
    private String startCity;
    @ApiModelProperty(value = "目的地")
    private String endCity;
    @ApiModelProperty(value = "提车起始日期")
    private Long startTime;
    @ApiModelProperty(value = "提车结束日期")
    private Long endTime;
    @ApiModelProperty(value = "业务中心ids，不需要传")
    private String storeIds;
}