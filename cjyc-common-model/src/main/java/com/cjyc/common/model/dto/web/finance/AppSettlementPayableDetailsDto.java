package com.cjyc.common.model.dto.web.finance;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: Hut
 * @Date: 2020/01/06 11:00
 * 应付账款-申请开票明细
 **/
@Data
public class AppSettlementPayableDetailsDto {

    @ApiModelProperty(value = "运单单号")
    private String wayBillNo;

    @ApiModelProperty(value = "承运商ID")
    private Long carrierId;
}
