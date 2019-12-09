package com.cjyc.common.model.vo.web.finance;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 等待开票
 */
@Data
public class WaitForBackVo extends WaitInvoiceVo {
    @ApiModelProperty(value = "发票号")
    private String invoiceNo;
}
