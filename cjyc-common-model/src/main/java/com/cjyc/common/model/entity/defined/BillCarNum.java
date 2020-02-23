package com.cjyc.common.model.entity.defined;

import lombok.Data;

@Data
public class BillCarNum {
    private Integer totalCarNum;
    private Integer unFinishCarNum;
    private Integer cancelCarNum;
}
