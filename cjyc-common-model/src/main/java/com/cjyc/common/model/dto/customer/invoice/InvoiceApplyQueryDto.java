package com.cjyc.common.model.dto.customer.invoice;

import com.cjyc.common.model.dto.BasePageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Description 开发票订单查询实体
 * @Author LiuXingXiang
 * @Date 2019/11/1 17:29
 **/
@Data
public class InvoiceApplyQueryDto extends BasePageDto {
    private static final long serialVersionUID = -1473863706963518462L;
    public interface InvoiceApplyOrderQuery{}
    public interface InvoiceOrderAndInvoiceApplyQuery{}

    @ApiModelProperty(value = "客户登录ID")
    @NotNull(groups = {InvoiceOrderAndInvoiceApplyQuery.class,InvoiceApplyOrderQuery.class},message = "客户登录ID不能为空")
    private Long loginId;

    @ApiModelProperty(value = "发票申请ID")
    @NotNull(groups = {InvoiceApplyOrderQuery.class},message = "发票申请ID不能为空")
    private Long invoiceApplyId;
}
