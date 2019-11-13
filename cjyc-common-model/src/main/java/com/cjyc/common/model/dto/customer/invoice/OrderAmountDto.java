package com.cjyc.common.model.dto.customer.invoice;

import com.cjyc.common.model.util.BigDecimalSerizlizer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Description 开票订单信息
 * @Author LiuXingXiang
 * @Date 2019/11/2 15:44
 **/
@Data
public class OrderAmountDto implements Serializable {
    private static final long serialVersionUID = -2927137879452233207L;
    @ApiModelProperty(value = "开票金额")
    @NotNull(message = "金额不能为空")
    @DecimalMin(value = "0",message = "金额必须是数字且不能为负数")
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal amount;

    @ApiModelProperty(value = "订单编号")
    @NotBlank(message = "订单编号不能为空")
    private String orderNo;
}
