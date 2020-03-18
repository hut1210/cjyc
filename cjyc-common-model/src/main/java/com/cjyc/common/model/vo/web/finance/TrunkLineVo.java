package com.cjyc.common.model.vo.web.finance;

import com.cjyc.common.model.serizlizer.BigDecimalSerizlizer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
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

    @ApiModelProperty(value = "运单号")
    private String wayBillNo;
    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty(value = "结算类型")
    private String  settleType;
    @ApiModelProperty(value = "应付运费")
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal freightFee;

    @ApiModelProperty(value = "已付运费")
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal paidFreightFee;
    @ApiModelProperty(value = "付款时间")
    private Long payTime;
    @ApiModelProperty(value = "付款状态")
    private String  payState;

    @ApiModelProperty(value = "承运商账户")
    private String  phone;
}
