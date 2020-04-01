package com.cjyc.common.model.vo.web.finance;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.cjyc.common.model.serizlizer.BigDecimalSerizlizer;
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
    @ApiModelProperty(value = "订单编号")
    @Excel(name = "订单编号" ,orderNum = "7")
    private String orderNo;

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
        if(type!=null && type==1){
            return "C端客户";
        }else if(type!=null && type==2){
            return "企业";
        }else if(type!=null && type==3){
            return "合伙人";
        }else{
            return "";
        }
    }

    @ApiModelProperty(value = "客户名称")
    @Excel(name = "客户名称" ,orderNum = "14")
    private String customerName;

}
