package com.cjyc.common.model.vo.web.waybill;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

@Data
public class ExportLocalListWaybillCarVo {

    @Excel(name = "运单状态", orderNum = "0")
    private String stateDesc;
    @Excel(name = "运单单号", orderNum = "1")
    private String waybillNo;
    @Excel(name = "类型", orderNum = "2")
    private String typeDesc;
    @Excel(name = "车辆编号", orderNum = "3")
    private String orderCarNo;
    @Excel(name = "VIN码", orderNum = "4")
    private String vin;
    @Excel(name = "品牌", orderNum = "5")
    private String brand;
    @Excel(name = "车系", orderNum = "6")
    private String model;
    @Excel(name = "是否新车", orderNum = "7")
    private String isNewDesc;
}
