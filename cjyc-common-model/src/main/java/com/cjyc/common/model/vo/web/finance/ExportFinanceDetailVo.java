package com.cjyc.common.model.vo.web.finance;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.cjyc.common.model.serizlizer.BigDecimalSerizlizer;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.cjyc.common.model.util.MoneyUtil;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author: Hut
 * @Date: 2020/03/17 13:40
 **/
@Data
public class ExportFinanceDetailVo implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "车辆编号")
    @Excel(name = "车辆编号" ,orderNum = "0")
    private String no;
    @ApiModelProperty(value = "品牌")
    @Excel(name = "品牌" ,orderNum = "1")
    private String brand;
    @ApiModelProperty(value = "车系")
    @Excel(name = "车系" ,orderNum = "2")
    private String model;
    @ApiModelProperty(value = "vin码")
    @Excel(name = "vin码" ,orderNum = "3")
    private String vin;

    @ApiModelProperty(value = "订单所属业务中心")
    @Excel(name = "订单所属业务中心" ,orderNum = "4")
    private String inputStoreName;

    @ApiModelProperty(value = "运单号")
    @Excel(name = "运单号" ,orderNum = "5")
    private String wayBillNo;

    @ApiModelProperty(value = "运单类型")
    @Excel(name = "运单类型" ,orderNum = "6")
    private String wayBillTypeName;

    @ApiModelProperty(value = "结算类型")
    @Excel(name = "运单类型" ,orderNum = "7")
    private String  settleType;

    @ApiModelProperty(value = "应付运费")
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal freightFee;

    @Excel(name = "应付运费" ,orderNum = "8",type = 10)
    private String freightFeeStr;

    public String getFreightFeeStr() {

        return String.valueOf(MoneyUtil.nullToZero(getFreightFee()).divide(new BigDecimal(100)));
    }

    @ApiModelProperty(value = "实付运费")
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal paidFreightFee;

    @Excel(name = "实付运费" ,orderNum = "9",type = 10)
    private String paidFreightFeeStr;

    public String getPaidFreightFeeStr() {
        return String.valueOf(MoneyUtil.nullToZero(getPaidFreightFee()).divide(new BigDecimal(100)));
    }

    @ApiModelProperty(value = "付款状态")
    @Excel(name = "付款状态" ,orderNum = "10")
    private String payState;

    @ApiModelProperty(value = "付款时间")
    private Long payTime;
    @ApiModelProperty(value = "付款时间")
    @Excel(name = "付款时间" ,orderNum = "11")
    private String payTimeStr;

    public String getPayTimeStr() {
        Long date = getPayTime();
        if (null == date || date <= 0L) {
            return "";
        }
        return LocalDateTimeUtil.formatLDT(LocalDateTimeUtil.convertLongToLDT(date), "yyyy-MM-dd HH:mm:ss");
    }

    @ApiModelProperty(value = "承运商类型")
    private String carrierType;

    @Excel(name = "承运商类型" ,orderNum = "12")
    private String carrierTypeStr;

    public String getCarrierTypeStr() {
        String type = getCarrierType();
        if (type != null) {
            if (type.equals("1")) {
                return "干线-个人承运商";
            }
            if (type.equals("2")) {
                return "干线-企业承运商";
            }
            if (type.equals("4")) {
                return "同城-代驾";
            }
            if (type.equals("5")) {
                return "同城-拖车";
            }
        }
        return "";
    }

    @ApiModelProperty(value = "承运商名称")
    @Excel(name = "承运商名称" ,orderNum = "13")
    private String carrierName;

    @ApiModelProperty(value = "承运商账号")
    @Excel(name = "承运商账号" ,orderNum = "14")
    private String carrierPhone;

    @ApiModelProperty(value = "司机提车地(城市)")
    @Excel(name = "司机提车地(城市)" ,orderNum = "15")
    private String pickUpPlace;

    @ApiModelProperty(value = "司机交付地(城市)")
    @Excel(name = "司机交付地(城市)" ,orderNum = "16")
    private String deliveryPlace;
}
