package com.cjyc.common.model.vo.web.invoice;

import com.cjyc.common.model.dto.customer.invoice.OrderAmountDto;
import com.cjyc.common.model.entity.CustomerInvoice;
import com.cjyc.common.model.serizlizer.BigDecimalSerizlizer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Description 发票明细 Vo
 * @Author LiuXingXiang
 * @Date 2019/11/4 16:30
 **/
@Data
public class InvoiceDetailVo implements Serializable {
    private static final long serialVersionUID = 5247559603604773055L;
    @ApiModelProperty(value = "开票金额")
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal amount;

    @ApiModelProperty(value = "订单号金额列表")
    private List<OrderAmountDto> orderAmountList;

    @ApiModelProperty(value = "发票信息")
    private CustomerInvoice customerInvoice;
}
