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
 * @Date: 2020/02/17 12:49
 */
@Data
public class ExportFinanceVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "车辆编号")
    @Excel(name = "车辆编号" ,orderNum = "0")
    private String no;
    @ApiModelProperty(value = "品牌")
    @Excel(name = "品牌" ,orderNum = "1")
    private String brand;
    @ApiModelProperty(value = "型号")
    @Excel(name = "型号" ,orderNum = "2")
    private String model;
    @ApiModelProperty(value = "vin码")
    @Excel(name = "vin码" ,orderNum = "3")
    private String vin;
    @ApiModelProperty(value = "订单编号")
    @Excel(name = "订单编号" ,orderNum = "4")
    private String orderNo;
    @ApiModelProperty(value = "所属大区")
    @Excel(name = "所属大区" ,orderNum = "5")
    private String largeArea;
    @ApiModelProperty(value = "订单所属业务中心")
    @Excel(name = "订单所属业务中心" ,orderNum = "6")
    private String inputStoreName;
    @ApiModelProperty(value = "订单始发地")
    @Excel(name = "订单始发地" ,orderNum = "7")
    private String startAddress;
    @ApiModelProperty(value = "订单目的地")
    @Excel(name = "订单目的地" ,orderNum = "8")
    private String endAddress;
    //@Excel(name = "交付日期" ,orderNum = "9")
    private Long deliveryDate;
    @Excel(name = "交付日期" ,orderNum = "9")
    @ApiModelProperty(value = "交付日期")

    private String deliveryDateStr;

    public String getDeliveryDateStr() {
        Long date = getDeliveryDate();
        if (null == date || date <= 0L) {
            return "";
        }
        return LocalDateTimeUtil.formatLDT(LocalDateTimeUtil.convertLongToLDT(date), "yyyy-MM-dd");
    }

    /*@ApiModelProperty(value = "客户Id")
    @Excel(name = "客户Id" ,orderNum = "10")
    private Long customerId;*/
    @ApiModelProperty(value = "客户类型：1个人，2企业，3合伙人 ")
    //@Excel(name = "客户类型" ,orderNum = "11")
    private Integer type;

    @Excel(name = "客户类型" ,orderNum = "11")
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
    @Excel(name = "客户名称" ,orderNum = "12")
    private String customerName;
    @ApiModelProperty(value = "结算类型(0：时付  1：账期)")
    //@Excel(name = "结算类型" ,orderNum = "13")
    private Integer  payMode;

    /*@Excel(name = "结算类型" ,orderNum = "13")
    private String payModeName;

    public String getPayModeName() {
        Integer type = getPayMode();
        if(type!=null && type==0){
            return "时付";
        }else if(type!=null && type==1){
            return "账期";
        }else{
            return "";
        }
    }*/

    @JsonSerialize(using = BigDecimalSerizlizer.class)
    @ApiModelProperty(value = "订单分摊金额")
    @Excel(name = "订单分摊金额" ,orderNum = "14")
    private BigDecimal feeShare;

    @JsonSerialize(using = BigDecimalSerizlizer.class)
    @ApiModelProperty(value = "应收运费")
    @Excel(name = "应收运费" ,orderNum = "15")
    private BigDecimal freightReceivable;

    /*@JsonSerialize(using = BigDecimalSerizlizer.class)
    @ApiModelProperty(value = "开票金额")
    @Excel(name = "车辆编号" ,orderNum = "16")
    private BigDecimal invoiceFee;

    @JsonSerialize(using = BigDecimalSerizlizer.class)
    @ApiModelProperty(value = "差额")
    @Excel(name = "车辆编号" ,orderNum = "17")
    private BigDecimal Difference;*/

    @JsonSerialize(using = BigDecimalSerizlizer.class)
    @ApiModelProperty(value = "实收金额")
    @Excel(name = "实收金额" ,orderNum = "18")
    private BigDecimal amountReceived;

    @ApiModelProperty(value = "收款时间")
    private Long receivedTime;

    @Excel(name = "收款时间" ,orderNum = "19")
    private String receivedTimeStr;
    public String getReceivedTimeStr() {
        Long date = getReceivedTime();
        if (null == date || date <= 0L) {
            return "";
        }
        return LocalDateTimeUtil.formatLDT(LocalDateTimeUtil.convertLongToLDT(date), "yyyy-MM-dd");
    }

    @JsonSerialize(using = BigDecimalSerizlizer.class)
    @ApiModelProperty(value = "提车成本")
    @Excel(name = "提车成本" ,orderNum = "20")
    private BigDecimal pickUpCarFee;

    @JsonSerialize(using = BigDecimalSerizlizer.class)
    @ApiModelProperty(value = "干线成本")
    @Excel(name = "干线成本" ,orderNum = "21")
    private BigDecimal trunkLineFee;

    @JsonSerialize(using = BigDecimalSerizlizer.class)
    @ApiModelProperty(value = "送车成本")
    @Excel(name = "送车成本" ,orderNum = "22")
    private BigDecimal carryCarFee;

    @JsonSerialize(using = BigDecimalSerizlizer.class)
    @ApiModelProperty(value = "收入合计")
    @Excel(name = "收入合计" ,orderNum = "23")
    private BigDecimal totalIncome;

    @JsonSerialize(using = BigDecimalSerizlizer.class)
    @ApiModelProperty(value = "成本合计")
    @Excel(name = "成本合计" ,orderNum = "24")
    private BigDecimal totalCost;

    @JsonSerialize(using = BigDecimalSerizlizer.class)
    @ApiModelProperty(value = "毛利")
    @Excel(name = "毛利" ,orderNum = "25")
    private BigDecimal grossProfit;


}
