package com.cjyc.common.model.vo.web.finance;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.cjyc.common.model.serizlizer.BigDecimalSerizlizer;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author: Hut
 * @Date: 2019/12/11 10:08
 */
@Data
public class PaidNewVo implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "运单Id")
    private Long waybillId;

    @ApiModelProperty(value = "运单单号")
    @Excel(name = "运单单号" ,orderNum = "0")
    private String waybillNo;

    @ApiModelProperty(value = "交付日期")
    @Excel(name = "交付日期" ,orderNum = "1")
    private Long completeTime;

    @ApiModelProperty(value = "结算类型")
    @Excel(name = "结算类型" ,orderNum = "2")
    private String settleType;

    @JsonSerialize(using = BigDecimalSerizlizer.class)
    @ApiModelProperty(value = "应付运费")
    @Excel(name = "应付运费" ,orderNum = "3")
    private BigDecimal freightFee;

    @ApiModelProperty(value = "运单类型")
    private Integer type;

    @Excel(name = "运单类型" ,orderNum = "4")
    private String waybillTypeStr;

    public String getWaybillTypeStr() {
        Integer type = getType();
        if(type!=null && type==1){
            return "提车运单";
        }else if(type!=null && type==2){
            return "干线运单";
        }else if(type!=null && type==3){
            return "送车运单";
        }else{
            return "";
        }
    }

    @ApiModelProperty(value = "运输线路")
    @Excel(name = "运输线路" ,orderNum = "5")
    private String guideLine;

    @ApiModelProperty(value = "承运商")
    @Excel(name = "承运商" ,orderNum = "6")
    private String carrierName;

    @ApiModelProperty(value = "司机名称")
    @Excel(name = "司机名称" ,orderNum = "7")
    private String driverName;

    @ApiModelProperty(value = "司机电话")
    @Excel(name = "司机电话" ,orderNum = "8")
    private String driverPhone;

    @ApiModelProperty(value = "车牌号")
    @Excel(name = "车牌号" ,orderNum = "9")
    private String vehiclePlateNo;

    @ApiModelProperty(value = "付款状态 0未付款 2已付款")
    @Excel(name = "付款状态" ,orderNum = "10")
    private String state;

    @ApiModelProperty(value = "付款操作人")
    @Excel(name = "付款操作人" ,orderNum = "11")
    private String operator;

    @ApiModelProperty(value = "持卡人")
    @Excel(name = "持卡人" ,orderNum = "12")
    private String cardName;

    @ApiModelProperty(value = "开户行")
    @Excel(name = "开户行" ,orderNum = "13")
    private String bankName;

    @ApiModelProperty(value = "银行卡号")
    @Excel(name = "银行卡号" ,orderNum = "14")
    private String cardNo;

    @ApiModelProperty(value = "付款时间")
    private Long payTime;

    @Excel(name = "付款时间" ,orderNum = "15")
    private String payTimeStr;

    public String getPayTimeStr() {
        Long date = getPayTime();
        if (null == date || date <= 0L) {
            return "";
        }
        return LocalDateTimeUtil.formatLDT(LocalDateTimeUtil.convertLongToLDT(date), "yyyy-MM-dd mm:HH:ss");
    }

}
