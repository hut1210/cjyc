package com.cjyc.common.model.dto.web.waybill;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GetDto {
    private Long waybillId;
    @ApiModelProperty("查询类型：0全部车辆，1待指派车辆，9全部正常车辆")
    private Long state;
}
