package com.cjyc.common.model.vo.web.finance;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.cjyc.common.model.enums.CardTypeEnum;
import com.cjyc.common.model.enums.waybill.WaybillTypeEnum;
import com.cjyc.common.model.serizlizer.BigDecimalSerizlizer;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.StringUtils;

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
    @Excel(name = "运单单号", orderNum = "0")
    private String waybillNo;

    @ApiModelProperty(value = "交付日期")
    private Long completeTime;

    @Excel(name = "交付日期", orderNum = "1")
    private String completeTimeStr;

    public String getCompleteTimeStr() {
        Long date = getCompleteTime();
        if (null == date || date <= 0L) {
            return "";
        }
        return LocalDateTimeUtil.formatLong(date, "yyyy-MM-dd HH:mm:ss");
    }

    @ApiModelProperty(value = "结算类型")
    @Excel(name = "结算类型", orderNum = "2")
    private String settleType;

    @JsonSerialize(using = BigDecimalSerizlizer.class)
    @ApiModelProperty(value = "应付运费")
    @Excel(name = "应付运费", orderNum = "3",type = 10)
    private BigDecimal freightFee;

    @JsonSerialize(using = BigDecimalSerizlizer.class)
    @ApiModelProperty(value = "实付运费")
    @Excel(name = "实付运费", orderNum = "4",type = 10)
    private BigDecimal freightFeePayable;

    @ApiModelProperty(value = "付款时间")
    private Long payTime;

    @Excel(name = "付款时间", orderNum = "5")
    private String payTimeStr;

    public String getPayTimeStr() {
        Long date = getPayTime();
        if (null == date || date <= 0L) {
            return "";
        }
        return LocalDateTimeUtil.formatLong(date, "yyyy-MM-dd HH:mm:ss");
    }

    @ApiModelProperty(value = "付款状态 0未付款 2已付款")
    @Excel(name = "付款状态", orderNum = "6")
    private String state;

    @ApiModelProperty(value = "付款失败原因")
    @Excel(name = "付款失败原因", orderNum = "7")
    private String failReason;

    @ApiModelProperty(value = "运单类型")
    private Integer type;

    @Excel(name = "运单类型", orderNum = "8")
    private String waybillTypeStr;

    public String getWaybillTypeStr() {
        Integer type = getType();
        if (type != null && type == WaybillTypeEnum.PICK.code) {
            return "提车运单";
        } else if (type != null && type == WaybillTypeEnum.TRUNK.code) {
            return "干线运单";
        } else if (type != null && type == WaybillTypeEnum.BACK.code) {
            return "送车运单";
        } else {
            return "";
        }
    }

    @ApiModelProperty(value = "指导线路")
    @Excel(name = "指导线路", orderNum = "9")
    private String guideLine;

    @ApiModelProperty(value = "承运商")
    @Excel(name = "承运商", orderNum = "10")
    private String carrierName;

    @ApiModelProperty(value = "司机名称")
    @Excel(name = "司机名称", orderNum = "11")
    private String driverName;

    @ApiModelProperty(value = "司机电话")
    @Excel(name = "司机电话", orderNum = "12")
    private String driverPhone;

    @ApiModelProperty(value = "车牌号")
    @Excel(name = "车牌号", orderNum = "13")
    private String vehiclePlateNo;

    @ApiModelProperty(value = "付款账户类型")
    private String cardType;

    @Excel(name = "付款账户类型", orderNum = "14")
    private String cardTypeStr;

    /**
     * 对公对私code和name的映射
     *
     * @return
     */
    public String getCardTypeStr() {
        String cardType = getCardType();
        if (StringUtils.isEmpty(cardType)) {
            return "";
        }
        if (CardTypeEnum.PUBLIC.code == Integer.parseInt(cardType)) {
            return CardTypeEnum.PUBLIC.name;
        } else if (CardTypeEnum.PRIVATE.code == Integer.parseInt(cardType)) {
            return CardTypeEnum.PRIVATE.name;
        } else {
            return "";
        }
    }

    @ApiModelProperty(value = "身份证号/法人身份证号")
    @Excel(name = "身份证号/法人身份证号", orderNum = "15")
    private String idCard;

    @ApiModelProperty(value = "持卡人")
    @Excel(name = "持卡人", orderNum = "16")
    private String cardName;

    @ApiModelProperty(value = "开户行")
    @Excel(name = "开户行", orderNum = "17")
    private String bankName;

    @ApiModelProperty(value = "银行卡号")
    @Excel(name = "银行卡号", orderNum = "18")
    private String cardNo;

    @ApiModelProperty(value = "公户账号所在省份")
    @Excel(name = "公户账号所在省份", orderNum = "19")
    private String provinceName;

    @ApiModelProperty(value = "公户账号所在城市")
    @Excel(name = "公户账号所在城市", orderNum = "20")
    private String areaName;

    @ApiModelProperty(value = "付款操作人")
    @Excel(name = "付款操作人", orderNum = "21")
    private String operator;

}
