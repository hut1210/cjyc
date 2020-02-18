package com.cjyc.common.model.dto.web.finance;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: Hut
 * @Date: 2020/02/18 21:25
 */
@Data
public class ExternalPaymentDto {

    @ApiModelProperty(value = "运单号集合")
    private List<Long> waybillIds;

    @ApiModelProperty(value = "登录人Id")
    private Long loginId;
}
