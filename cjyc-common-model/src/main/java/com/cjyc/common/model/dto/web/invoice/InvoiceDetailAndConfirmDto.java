package com.cjyc.common.model.dto.web.invoice;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Description 发票明细与确认开票参数实体
 * @Author LiuXingXiang
 * @Date 2019/11/5 9:11
 **/
@Data
public class InvoiceDetailAndConfirmDto implements Serializable {
    private static final long serialVersionUID = -7550362779463566649L;
    public interface GetDetail{}
    public interface ConfirmInvoice{}

    @ApiModelProperty(value = "客户Id")
    @NotNull(groups = {GetDetail.class,ConfirmInvoice.class},message = "客户Id不能为空")
    private Long userId;

    @ApiModelProperty(value = "发票申请ID")
    @NotNull(groups = {GetDetail.class,ConfirmInvoice.class},message = "发票申请ID不能为空")
    private Long invoiceApplyId;

    @ApiModelProperty(value = "发票号")
    @NotBlank(groups = {ConfirmInvoice.class},message = "发票号不能为空")
    private String invoiceNo;
}
