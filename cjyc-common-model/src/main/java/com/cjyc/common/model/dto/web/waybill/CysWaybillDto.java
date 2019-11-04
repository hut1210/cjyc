package com.cjyc.common.model.dto.web.waybill;

import com.cjyc.common.model.dto.BasePageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CysWaybillDto extends BasePageDto {

    @ApiModelProperty(value = "司机ID",required = true)
    private Long driverId;
    @ApiModelProperty(value = "承运商ID",required = true)
    private Long carrierId;


}
