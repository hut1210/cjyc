package com.cjyc.common.model.vo.web.finance;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>应收账款-待开票-确认开票请求参数Vo</p>
 *
 * @Author:RenPL
 * @Date:2020/3/20 15:57
 */
@Data
public class ConfirmInvoiceVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "结算流水号")
    private String serialNumber;

    @ApiModelProperty(value = "发票号")
    private String invoiceNo;

    @ApiModelProperty(value = "业务员Id")
    private Long customerId;

    @ApiModelProperty(value = "业务员名称")
    private String customerName;


}
