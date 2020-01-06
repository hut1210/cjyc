package com.cjyc.common.model.vo.web.finance;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: Hut
 * @Date: 2020/01/06 11:08
 **/
@Data
public class SettlementPayableVo {

    @ApiModelProperty(value = "运单单号")
    private String no;

    @ApiModelProperty(value = "应付运费")
    private BigDecimal freightFee;

    @ApiModelProperty(value = "运单类型")
    private Integer type;

    @ApiModelProperty(value = "车辆数")
    private int carNum;
}
