package com.cjyc.common.model.vo.web.finance;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author:Hut
 * @Date:2019/11/19 15:29
 */
@Data
public class TrunkLineVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty(value = "结算类型")
    private String  settleType;
    @ApiModelProperty(value = "应付运费")
    private BigDecimal freightFee;
    @ApiModelProperty(value = "付款时间")
    private Long payTime;
}
