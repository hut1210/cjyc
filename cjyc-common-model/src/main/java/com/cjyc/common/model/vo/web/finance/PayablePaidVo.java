package com.cjyc.common.model.vo.web.finance;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: Hut
 * @Date: 2020/01/06 17:15
 **/
@Data
public class PayablePaidVo extends SettlementVo{

    @ApiModelProperty(value = "差额")
    private BigDecimal difference;
}
