package com.cjyc.common.model.dto.web.order;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class OrderCarNoDto {
    @NotBlank(message = "订单车辆编号不能为空")
    private String orderCarNo;
}
