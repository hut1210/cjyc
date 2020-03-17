package com.cjyc.common.model.dto.web.publicPayBank;

import com.cjyc.common.model.dto.BasePageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class PayBankDto extends BasePageDto implements Serializable {
    private static final long serialVersionUID = 5777438693363192382L;

    @ApiModelProperty(value = "支行名称")
    private String subBankName;

    @ApiModelProperty(value = "支付行号")
    private String payBankNo;
}