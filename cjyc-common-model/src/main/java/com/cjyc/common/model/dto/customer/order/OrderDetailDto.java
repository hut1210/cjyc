package com.cjyc.common.model.dto.customer.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Description 更新状态实体类
 * @Author LiuXingXiang
 * @Date 2019/11/1 15:11
 **/
@Data
public class OrderDetailDto implements Serializable {
    private static final long serialVersionUID = -8045045839903456346L;
    @ApiModelProperty("订单号")
    @NotBlank(message = "订单号不能为空")
    private String orderNo;

    @ApiModelProperty("客户id")
    @NotNull(message = "客户id不能为空" )
    private Long loginId;
}
