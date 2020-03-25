package com.yqzl.model.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 交行转账数据封装对象
 *
 * @author RenPL 2020-3-15
 */
@Data
public class FundTransferResponse {

    @ApiModelProperty(value = "交易时间")
    private Integer trTime;

    @ApiModelProperty(value = "交易流水号")
    private String serialNo;

    public FundTransferResponse(Integer trTime, String serialNo) {
        this.trTime = trTime;
        this.serialNo = serialNo;
    }
}
