package com.cjyc.common.model.vo.web.finance;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.cjyc.common.model.enums.waybill.WaybillTypeEnum;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author: Hut
 * @Date: 2020/01/03 11:03
 **/
@Data
public class FinancePayableVo implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "运单单号")
    @Excel(name = "运单单号" ,orderNum = "0")
    private String no;

    @ApiModelProperty(value = "交付日期")
    private Long completeTime;

    @Excel(name = "交付日期" ,orderNum = "1")
    private String completeTimeStr;

    public String getCompleteTimeStr() {
        Long date = getCompleteTime();
        if (null == date || date <= 0L) {
            return "";
        }
        return LocalDateTimeUtil.formatLDT(LocalDateTimeUtil.convertLongToLDT(date), "yyyy-MM-dd HH:mm:ss");
    }

    @ApiModelProperty(value = "结算类型")
    private Integer type;

    @ApiModelProperty(value = "结算类型名称")
    @Excel(name = "结算类型" ,orderNum = "2")
    private String settleTypeName;

    @ApiModelProperty(value = "账期时间")
    @Excel(name = "账期时间" ,orderNum = "3")
    private int settlePeriod;

    @ApiModelProperty(value = "剩余账期时间")
    @Excel(name = "剩余账期时间" ,orderNum = "4")
    private Long remainDate;

    @ApiModelProperty(value = "应付运费")
    @Excel(name = "应付运费" ,orderNum = "5",type = 10)
    private BigDecimal freightPayable;

    @ApiModelProperty(value = "运单类型 1提车运单，2干线运单，3送车运单")
    private Integer waybillType;

    @Excel(name = "运单类型" ,orderNum = "6")
    private String waybillTypeStr;

    public String getWaybillTypeStr() {
        Integer type = getWaybillType();
        if(type!=null && type== WaybillTypeEnum.PICK.code){
            return "提车运单";
        }else if(type!=null && type==WaybillTypeEnum.TRUNK.code){
            return "干线运单";
        }else if(type!=null && type==WaybillTypeEnum.BACK.code){
            return "送车运单";
        }else{
            return "";
        }
    }

    @ApiModelProperty(value = "指导路线")
    @Excel(name = "指导路线" ,orderNum = "7")
    private String transportLine;

    @ApiModelProperty(value = "承运商ID")
    private Long carrierId;

    @ApiModelProperty(value = "承运类型")
    private Integer carrierType;

    @ApiModelProperty(value = "承运商名称")
    @Excel(name = "承运商名称" ,orderNum = "8")
    private String carrierName;

    @ApiModelProperty(value = "司机名称")
    @Excel(name = "司机名称" ,orderNum = "9")
    private String driverName;

    @ApiModelProperty(value = "司机电话")
    @Excel(name = "司机电话" ,orderNum = "10")
    private String driverPhone;

    @ApiModelProperty(value = "车牌号")
    @Excel(name = "车牌号" ,orderNum = "11")
    private String vehiclePlateNo;

    @ApiModelProperty(value = "车辆数")
    private int carNum;
}
