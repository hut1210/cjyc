package com.cjyc.common.model.dto.salesman.mine;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class SalesAchieveDto implements Serializable {
    private static final long serialVersionUID = -4487695836279366634L;

    @ApiModelProperty(value = "本月起始日时间戳")
    private Long thisMonthTime;
    @ApiModelProperty(value = "下一个月起始日时间戳")
    private Long nextMonthTime;
}