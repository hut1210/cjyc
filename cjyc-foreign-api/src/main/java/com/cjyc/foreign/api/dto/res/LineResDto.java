package com.cjyc.foreign.api.dto.res;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Description 班线返回参数
 * @Author Liu Xing Xiang
 * @Date 2020/3/10 10:19
 **/
@Data
public class LineResDto implements Serializable {
    private static final long serialVersionUID = 1823148747037611381L;
    @ApiModelProperty(value = "出发地城市名称")
    private String fromCity;

    @ApiModelProperty(value = "目的地城市名称")
    private String toCity;

    @ApiModelProperty(value = "默认物流费（上游），单位分")
    private BigDecimal defaultWlFee;
}
