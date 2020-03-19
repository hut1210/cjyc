package com.cjyc.common.model.vo.customer.customerInfo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
@Data
public class CustomerCardInfoVo implements Serializable {
    private static final long serialVersionUID = 3423369739383814445L;

    @ApiModelProperty("绑定银行卡id")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long cardId;

    @ApiModelProperty(value = "卡类型:1公户，2私户")
    private Integer cardType;

    @ApiModelProperty(value = "银行卡户主")
    private String cardName;

    @ApiModelProperty(value = "省/直辖市名称")
    private String provinceName;

    @ApiModelProperty(value = "地区名称")
    private String areaName;

    @ApiModelProperty("银行(支行)名称")
    private String bankName;

    @ApiModelProperty("银行卡号")
    private String cardNo;

    @ApiModelProperty("颜色类型 1：红色 2：蓝色 3：绿色")
    private Integer cardColour;

    public Long getCardId(){return cardId == null ? 0 : cardId;}
    public Integer getCardType(){return cardType == null ? 0 : cardType;}
    public Integer getCardColour(){return cardColour == null ? 0 : cardColour;}
    public String getCardName(){return StringUtils.isBlank(cardName) ? "" : cardName;}
    public String getProvinceName(){return StringUtils.isBlank(provinceName) ? "" : provinceName;}
    public String getAreaName(){return StringUtils.isBlank(areaName) ? "" : areaName;}
    public String getBankName(){return StringUtils.isBlank(bankName) ? "" : bankName;}
    public String getCardNo(){return StringUtils.isBlank(cardNo) ? "" : cardNo;}
}