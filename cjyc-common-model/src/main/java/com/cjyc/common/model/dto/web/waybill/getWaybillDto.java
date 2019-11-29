package com.cjyc.common.model.dto.web.waybill;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class getWaybillDto {

    private Long waybillId;
    @ApiModelProperty(value = "状态：0全部，1待指派")
    private Integer state;
}
