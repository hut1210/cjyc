package com.cjyc.common.model.dto.web.finance;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author: Hut
 * @Date: 2020/01/06 15:33
 * 应付账款-确认开票
 **/
@Data
public class ConfirmTicketDto {

    @ApiModelProperty(value = "结算流水号")
    private String serialNumber;

    @ApiModelProperty(value = "发票号")
    private String invoiceNo;

    @NotNull(message = "确认人Id不能为空")
    @ApiModelProperty(value = "确认人Id", required = true)
    private Long confirmId;
}
