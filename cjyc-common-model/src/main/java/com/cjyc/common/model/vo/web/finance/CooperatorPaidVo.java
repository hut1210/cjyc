package com.cjyc.common.model.vo.web.finance;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author: Hut
 * @Date: 2020/03/09 10:48
 **/
@Data
public class CooperatorPaidVo implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "订单Id")
    private Long orderId;

    @ApiModelProperty(value = "订单编号")
    @Excel(name = "订单编号" ,orderNum = "0")
    private String orderNo;

    @ApiModelProperty(value = "合伙人服务费")
    @Excel(name = "伙人服务费" ,orderNum = "1")
    private BigDecimal serviceFee;

    @ApiModelProperty(value = "付款状态")
    @Excel(name = "付款状态" ,orderNum = "2")
    private String state;

    @ApiModelProperty(value = "付款失败原因")
    @Excel(name = "付款失败原因" ,orderNum = "3")
    private String description;

    @ApiModelProperty(value = "付款时间")
    private Long payTime;

    @Excel(name = "付款时间" ,orderNum = "4")
    private String payTimeStr;

    public String getPayTimeStr() {
        Long date = getPayTime();
        if (null == date || date <= 0L) {
            return "";
        }
        return LocalDateTimeUtil.formatLDT(LocalDateTimeUtil.convertLongToLDT(date), "yyyy-MM-dd mm:HH:ss");
    }

    @ApiModelProperty(value = "结算类型")
    @Excel(name = "结算类型" ,orderNum = "5")
    private String settleType;

    @ApiModelProperty(value = "始发城市")
    @Excel(name = "始发城市" ,orderNum = "6")
    private String startCity;

    @ApiModelProperty(value = "目的城市")
    @Excel(name = "目的城市" ,orderNum = "7")
    private String endCity;

    @ApiModelProperty(value = "下单客户")
    @Excel(name = "下单客户" ,orderNum = "8")
    private String customerPhone;

    @ApiModelProperty(value = "客户Id")
    private Long customerId;

    @ApiModelProperty(value = "客户名称")
    @Excel(name = "客户名称" ,orderNum = "9")
    private String customerName;

    @ApiModelProperty(value = "客户支付方式 0到付（默认），1预付，2账期")
    @Excel(name = "客户支付方式" ,orderNum = "10")
    private String customerPayType;

    @ApiModelProperty(value = "客户支付时间")
    private Long wlPayTime;

    @Excel(name = "客户支付时间" ,orderNum = "11")
    private String wlPayTimeStr;

    public String getWlPayTimeStr() {
        Long date = getWlPayTime();
        if (null == date || date <= 0L) {
            return "";
        }
        return LocalDateTimeUtil.formatLDT(LocalDateTimeUtil.convertLongToLDT(date), "yyyy-MM-dd mm:HH:ss");
    }

    @ApiModelProperty(value = "客户交付时间")
    private Long completeTime;

    @Excel(name = "客户交付时间" ,orderNum = "12")
    private String completeTimeStr;

    public String getCompleteTimeStr() {
        Long date = getCompleteTime();
        if (null == date || date <= 0L) {
            return "";
        }
        return LocalDateTimeUtil.formatLDT(LocalDateTimeUtil.convertLongToLDT(date), "yyyy-MM-dd mm:HH:ss");
    }

    @ApiModelProperty(value = "订单金额")
    @Excel(name = "订单金额" ,orderNum = "13")
    private BigDecimal totalFee;

    @ApiModelProperty(value = "总费用")
    @Excel(name = "总费用" ,orderNum = "14")
    private BigDecimal wlFee;

    @ApiModelProperty(value = "付款类型（公户，个户）")
    @Excel(name = "付款类型" ,orderNum = "15")
    private String cardType;

    @ApiModelProperty(value = "对公开户名称")
    private String publicAccount;

    @ApiModelProperty(value = "开户银行账号")
    @Excel(name = "开户银行账号" ,orderNum = "16")
    private String cardNo;

    @ApiModelProperty(value = "开户行名称")
    @Excel(name = "开户行名称" ,orderNum = "17")
    private String bankName;

    @ApiModelProperty(value = "持卡人姓名")
    @Excel(name = "持卡人姓名" ,orderNum = "18")
    private String cardName;

    @ApiModelProperty(value = "持卡人身份证号")
    @Excel(name = "持卡人身份证号" ,orderNum = "19")
    private String IDCard;

    @ApiModelProperty(value = "付款操作人")
    @Excel(name = "付款操作人" ,orderNum = "20")
    private String operator;

}
