package com.cjyc.common.model.dto.web.vehicle;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
@Data
public class VehicleImportExcel implements Serializable {
    private static final long serialVersionUID = -785214520598549260L;

    @Excel(name = "车牌号" ,orderNum = "0")
    private String plateNo;

    @Excel(name = "车位数" ,orderNum = "1")
    private Integer defaultCarryNum;
}