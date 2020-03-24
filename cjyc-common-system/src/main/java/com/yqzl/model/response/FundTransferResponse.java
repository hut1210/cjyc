package com.yqzl.model.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 银行转账返回对象
 *
 * @author RenPL 2020-3-15
 */
@Data
public class FundTransferResponse {

    @ApiModelProperty(value = "交易码", required = true)
    private String trCode;




}
