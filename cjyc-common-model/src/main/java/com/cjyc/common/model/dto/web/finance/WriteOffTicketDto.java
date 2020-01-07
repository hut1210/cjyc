package com.cjyc.common.model.dto.web.finance;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @Author: Hut
 * @Date: 2020/01/06 16:45
 **/
@Data
public class WriteOffTicketDto {

    @ApiModelProperty(value = "结算流水号")
    private String serialNumber;

    @NotNull(message = "核销人Id不能为空")
    @ApiModelProperty(value = "核销人Id", required = true)
    private Long writeOffId;

    @ApiModelProperty(value = "实付总运费")
    private BigDecimal totalFreightPay;
}
