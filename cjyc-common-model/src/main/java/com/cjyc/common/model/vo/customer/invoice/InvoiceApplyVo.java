package com.cjyc.common.model.vo.customer.invoice;

import com.cjyc.common.model.serizlizer.BigDecimalSerizlizer;
import com.cjyc.common.model.serizlizer.DateLongSerizlizer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Description 开票申请记录
 * @Author LiuXingXiang
 * @Date 2019/11/12 17:24
 **/
@Data
public class InvoiceApplyVo implements Serializable {
    private static final long serialVersionUID = 2794677299505293380L;
    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "发票ID")
    private Long invoiceId;

    @ApiModelProperty(value = "客户ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long customerId;

    @ApiModelProperty(value = "申请人名称")
    private String customerName;

    @ApiModelProperty(value = "申请时间")
    private Long applyTime;

    @ApiModelProperty(value = "开票时间")
    private Long invoiceTime;

    @ApiModelProperty(value = "开票金额")
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal amount;

    @ApiModelProperty(value = "发票号")
    private String invoiceNo;

    @ApiModelProperty(value = "开票人姓名")
    private String operationName;

    @ApiModelProperty(value = "开票状态 1-申请中，2-已开票")
    private Integer state;

    public String getCustomerName() {
        return StringUtils.isBlank(customerName) ? "" : customerName;
    }

    public String getInvoiceNo() {
        return StringUtils.isBlank(invoiceNo) ? "" : invoiceNo;
    }

    public String getOperationName() {
        return StringUtils.isBlank(operationName) ? "" : operationName;
    }

    public Long getApplyTime() {
        return applyTime == null ? 0 : applyTime;
    }

    public Long getInvoiceTime() {
        return invoiceTime == null ? 0 : invoiceTime;
    }

    public BigDecimal getAmount() {
        return amount == null ? new BigDecimal(0) : amount;
    }
}
