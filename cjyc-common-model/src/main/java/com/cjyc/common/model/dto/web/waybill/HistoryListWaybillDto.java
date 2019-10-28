package com.cjyc.common.model.dto.web.waybill;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel
public class HistoryListWaybillDto {

    @ApiModelProperty(value = "订单车辆ID",required = true)
    private Long orderCarId;

    @ApiModelProperty(value = "运单类型：1提车运单，2干线运单，3送车运单",required = true)
    private Long type;

}
