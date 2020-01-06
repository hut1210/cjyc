package com.cjyc.common.model.dto.web.finance;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Value;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: Hut
 * @Date: 2020/01/06 11:00
 * 应付账款-申请开票
 **/
@Data
public class AppSettlementPayableDto {

    @NotNull(message = "操作人id不能为空")
    @ApiModelProperty(value = "操作人id", required = true)
    private Long loginId;

    @ApiModelProperty(value = "运单单号集合")
    private List<String> taskNo;

    @ApiModelProperty(value = "应付总运费")
    private BigDecimal freightFee;
}
