package com.cjyc.common.model.vo.web.mineCarrier;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
@Data
public class CarrierDriversImportExcel implements Serializable {
    private static final long serialVersionUID = 2791674308323668477L;

    @Excel(name = "司机姓名" ,orderNum = "0")
    private String realName;

    @Excel(name = "司机手机号" ,orderNum = "1")
    private String phone;

    @Excel(name = "身份证号" ,orderNum = "2")
    private String idCard;

    @Excel(name = "司机类型(代驾/拖车/干线 中的一种)" ,orderNum = "3")
    private String mode;

    @Excel(name = "车牌号" ,orderNum = "4")
    private String plateNo;

    @Excel(name = "车位数" ,orderNum = "5")
    private Integer defaultCarryNum;
}