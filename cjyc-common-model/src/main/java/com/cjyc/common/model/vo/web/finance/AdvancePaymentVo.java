package com.cjyc.common.model.vo.web.finance;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.cjyc.common.model.enums.customer.ClientTypeEnum;
import com.cjyc.common.model.enums.customer.CustomerPayTypeEnum;
import com.cjyc.common.model.enums.customer.CustomerTypeEnum;
import com.cjyc.common.model.serizlizer.BigDecimalSerizlizer;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author: Hut
 * @Date: 2020/03/30 11:08
 **/
@Data
public class AdvancePaymentVo implements Serializable {

    @ApiModelProperty(value = "车辆编号")
    @Excel(name = "车辆编号" ,orderNum = "0")
    private String no;
    @ApiModelProperty(value = "vin码")
    @Excel(name = "vin码" ,orderNum = "1")
    private String vin;
    @ApiModelProperty(value = "品牌")
    @Excel(name = "品牌" ,orderNum = "2")
    private String brand;
    @ApiModelProperty(value = "车系")
    @Excel(name = "车系" ,orderNum = "3")
    private String model;

    @JsonSerialize(using = BigDecimalSerizlizer.class)
    @ApiModelProperty(value = "应收运费")
    @Excel(name = "应收运费" ,orderNum = "5",type = 10)
    private BigDecimal freightReceivable;

    @JsonSerialize(using = BigDecimalSerizlizer.class)
    @ApiModelProperty(value = "实收运费")
    @Excel(name = "实收运费" ,orderNum = "6",type = 10)
    private BigDecimal freightPay;

    @ApiModelProperty(value = "收款时间")
    private Long payTime;

    @ApiModelProperty(value = "收款时间")
    @Excel(name = "收款时间" ,orderNum = "7")
    private String payTimeStr;

    public String getPayTimeStr() {
        Long date = getPayTime();
        if (null == date || date <= 0L) {
            return "";
        }
        return LocalDateTimeUtil.formatLong(date, "yyyy-MM-dd HH:mm:ss");
    }

    @ApiModelProperty(value = "订单编号")
    @Excel(name = "订单编号" ,orderNum = "8")
    private String orderNo;

    @ApiModelProperty(value = "支付方式：0到付（默认），1预付，2账期")
    private Integer payType;

    @ApiModelProperty(value = "支付方式：0到付（默认），1预付，2账期")
    @Excel(name = "支付方式" ,orderNum = "9")
    private String payTypeStr;

    public String getPayTypeStr() {
        Integer type = getPayType();
        if(type!=null && type== CustomerPayTypeEnum.TO_PAY.code){
            return CustomerPayTypeEnum.TO_PAY.name;
        }else if(type!=null && type==CustomerPayTypeEnum.PRE_PAY.code){
            return CustomerPayTypeEnum.PRE_PAY.name;
        }else if(type!=null && type==CustomerPayTypeEnum.PERIOD_PAY.code){
            return CustomerPayTypeEnum.PERIOD_PAY.name;
        }else{
            return "";
        }
    }

    @ApiModelProperty(value = "始发地")
    @Excel(name = "始发地" ,orderNum = "10")
    private String startCity;

    @ApiModelProperty(value = "目的地")
    @Excel(name = "目的地" ,orderNum = "11")
    private String endCity;

    @ApiModelProperty(value = "客户类型")
    private Integer type;

    @Excel(name = "客户类型" ,orderNum = "13")
    private String customTypeName;

    public String getCustomTypeName() {
        Integer type = getType();
        if(type!=null && type== ClientTypeEnum.INDIVIDUAL.code){
            return ClientTypeEnum.INDIVIDUAL.name;
        }else if(type!=null && type==CustomerTypeEnum.ENTERPRISE.code){
            return ClientTypeEnum.ENTERPRISE.name;
        }else if(type!=null && type==CustomerTypeEnum.COOPERATOR.code){
            return ClientTypeEnum.COOPERATOR.name;
        }else{
            return "";
        }
    }

    @ApiModelProperty(value = "客户名称")
    @Excel(name = "客户名称" ,orderNum = "14")
    private String customerName;

}
