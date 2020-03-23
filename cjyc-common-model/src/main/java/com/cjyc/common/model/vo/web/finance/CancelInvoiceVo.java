package com.cjyc.common.model.vo.web.finance;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>应收账款-待开票-取消开票请求参数Vo</p>
 *
 * @Author:RenPL
 * @Date:2020/3/20 15:57
 */
@Data
public class CancelInvoiceVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "结算流水号", required = true)
    private String serialNumber;

    @ApiModelProperty(value = "业务员Id", required = true)
    private Long customerId;

    @ApiModelProperty(value = "业务员名称", required = false)
    private String customerName;


}
