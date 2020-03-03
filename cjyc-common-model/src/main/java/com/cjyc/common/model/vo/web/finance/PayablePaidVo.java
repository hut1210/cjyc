package com.cjyc.common.model.vo.web.finance;

import cn.afterturn.easypoi.excel.annotation.Excel;
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
    @Excel(name = "差额" ,orderNum = "10")
    private BigDecimal difference;
}
