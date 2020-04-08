package com.cjyc.common.model.vo.web.waybill;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.cjyc.common.model.constant.TimePatternConstant;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;

@Data
public class ExportTrunkSubListWaybillVo implements Serializable {
    private static final long serialVersionUID = -3929877068505317681L;
    @Excel(name = "运单单号", orderNum = "0",width = 20)
    private String wtNo;
    @Excel(name = "状态", orderNum = "1",width = 15)
    private String outterState;
    @Excel(name = "运费(元)", orderNum = "2",width = 15,type = 10)
    private BigDecimal freightFee;
    @Excel(name = "指导线路", orderNum = "3",width = 20)
    private String guideLine;
    @Excel(name = "运输车辆数", orderNum = "4",width = 15)
    private Integer carNum;
    @Excel(name = "承运商", orderNum = "5",width = 20)
    private String carrierName;
    @Excel(name = "司机", orderNum = "6",width = 20)
    private String driverName;
    @Excel(name = "司机电话", orderNum = "7",width = 20)
    private String driverPhone;
    @Excel(name = "车牌号", orderNum = "8",width = 15)
    private String vehiclePlateNo;
    @Excel(name = "动态车位", orderNum = "9",width = 15)
    private String dynamicCarryNum;
    @Excel(name = "备注信息", orderNum = "10",width = 15)
    private String remark;
    @Excel(name = "创建时间", orderNum = "11",width = 20)
    private Long createTime;
    @Excel(name = "创建人", orderNum = "12",width = 25)
    private String createUser;
    private Integer carryCarNum;
    private Integer occupiedCarNum;

    public String getCreateTime(){
        if(createTime != null){
            return LocalDateTimeUtil.formatLDT(LocalDateTimeUtil.convertLongToLDT(createTime), TimePatternConstant.DATETIME);
        }
        return "";
    }
    public String getDynamicCarryNum() {
        Integer oc = getOccupiedCarNum();
        Integer tc = getCarryCarNum();
        return (oc == null?"0": oc+"") + "/" + (tc == null?"0": tc+"");
    }
    public String getFreightFee() {
        if (freightFee == null) {
            return "0";
        }
        DecimalFormat df =new DecimalFormat("0.00");
        BigDecimal value = freightFee.divide(BigDecimal.valueOf(100));
        return freightFee.equals(BigDecimal.ZERO) ? "0" : df.format(value);
    }

}