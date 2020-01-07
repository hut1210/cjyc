package com.cjyc.common.model.dto.web.finance;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: Hut
 * @Date: 2020/01/06 17:05
 **/
@Data
public class PayablePaidQueryDto extends WaitPaymentDto {

    @ApiModelProperty(value = "核销开始时间")
    private  Long writeOffStartTime;

    @ApiModelProperty(value = "核销结束时间")
    private  Long writeOffEndTime;

    @ApiModelProperty(value = "核销人")
    private  String writeOffMan;
}
