package com.cjyc.common.model.dto.web.finance;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: Hut
 * @Date: 2020/01/06 16:09
 **/
@Data
public class WaitPaymentDto extends WaitTicketCollectDto{

    @ApiModelProperty(value = "发票号")
    private String invoiceNo;

    @ApiModelProperty(value = "确认收票开始时间")
    private  Long confirmStartTime;

    @ApiModelProperty(value = "确认收票结束时间")
    private  Long confirmEndTime;

    @ApiModelProperty(value = "确认人")
    private  String confirmMan;
}
