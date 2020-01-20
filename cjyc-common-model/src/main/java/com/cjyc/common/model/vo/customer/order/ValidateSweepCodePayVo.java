package com.cjyc.common.model.vo.customer.order;

import com.cjyc.common.model.serizlizer.BigDecimalSerizlizer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: Hut
 * @Date: 2019/12/11 14:39
 */
@Data
public class ValidateSweepCodePayVo {

    @ApiModelProperty(value = "是否需要支付：0否 ，1是")
    private Integer isNeedPay;

    @ApiModelProperty(value = "金额")
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal amount;

    @ApiModelProperty(value = "任务Id")
    private Long taskId;

    @ApiModelProperty(value = "Ids")
    private List<Long> taskCarIds;


}
