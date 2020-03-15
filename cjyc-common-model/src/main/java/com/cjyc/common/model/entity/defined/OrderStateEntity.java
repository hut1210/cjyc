package com.cjyc.common.model.entity.defined;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderStateEntity {
    private String orderNo;
    private Integer state;
    private Long createTime;

}
