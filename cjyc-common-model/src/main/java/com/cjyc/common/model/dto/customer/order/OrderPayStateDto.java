package com.cjyc.common.model.dto.customer.order;

import lombok.Data;

import java.util.List;

@Data
public class OrderPayStateDto {

    private Long loginId;
    private List<Long> orderCarId;
}
