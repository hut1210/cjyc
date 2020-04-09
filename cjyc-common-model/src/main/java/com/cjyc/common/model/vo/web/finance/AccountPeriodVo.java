package com.cjyc.common.model.vo.web.finance;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Hut
 * @Date: 2020/04/09 15:06
 **/
@Data
public class AccountPeriodVo implements Serializable {

    @ApiModelProperty(value = "车辆编号")
    private String taskNo;

    @ApiModelProperty(value = "核销时间")
    private Long writeOffTime;
}
