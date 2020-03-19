package com.cjyc.common.model.vo.web.payBank;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class PayBankVo implements Serializable {
    private static final long serialVersionUID = 5908523451295835862L;

    @ApiModelProperty(value = "对公支付银行id")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "银行编码")
    private String bankCode;

    @ApiModelProperty(value = "支付行号")
    private String payBankNo;

    @ApiModelProperty(value = "支行名称")
    private String subBankName;

    @ApiModelProperty(value = "地区编号")
    private String areaCode;


}