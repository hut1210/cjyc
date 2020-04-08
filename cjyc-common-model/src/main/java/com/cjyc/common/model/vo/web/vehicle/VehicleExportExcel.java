package com.cjyc.common.model.vo.web.vehicle;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.cjyc.common.model.constant.TimePatternConstant;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import lombok.Data;

import java.io.Serializable;
@Data
public class VehicleExportExcel implements Serializable {
    private static final long serialVersionUID = 3456273976636745401L;
    @Excel(name = "车牌号" ,orderNum = "0",width = 25)
    private String plateNo;

    @Excel(name = "车位数" ,orderNum = "1",width = 25)
    private Integer defaultCarryNum;

    @Excel(name = "司机姓名" ,orderNum = "2",width = 25)
    private String realName;

    @Excel(name = "司机电话" ,orderNum = "3",width = 25)
    private String phone;

    @Excel(name = "操作时间" ,orderNum = "4",width = 25)
    private Long checkTime;

    @Excel(name = "操作人" ,orderNum = "5",width = 25)
    private String checkName;

    public String getCheckTime(){
        if(checkTime != null){
            return LocalDateTimeUtil.formatLDT(LocalDateTimeUtil.convertLongToLDT(checkTime), TimePatternConstant.DATETIME);
        }
        return "";
    }
}