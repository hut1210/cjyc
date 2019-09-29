package com.cjyc.web.api.dto;

import lombok.Data;

/**
 * @auther litan
 * @description: com.cjyc.web.api.dto
 * @date:2019/9/29
 */
@Data(staticConstructor = "getInstance")
public class OrderDto {
    private Long id;
    private String orderCode;
    private String customerName;
}
