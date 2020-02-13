package com.cjyc.common.model.vo.web.bankInfo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class BankInfoVo implements Serializable {
    private static final long serialVersionUID = 5469650968696481598L;

    @ApiModelProperty(value = "银行编码")
    private String bankCode;
    @ApiModelProperty(value = "银行名称")
    private String bankName;
}