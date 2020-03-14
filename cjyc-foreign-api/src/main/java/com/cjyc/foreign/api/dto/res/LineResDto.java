package com.cjyc.foreign.api.dto.res;

import com.cjyc.common.model.serizlizer.BigDecimalSerizlizer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
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
    @ApiModelProperty(value = "线路ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "起点城市名称")
    private String fromCity;

    @ApiModelProperty(value = "终点城市名称")
    private String toCity;

    @ApiModelProperty(value = "报价(元)")
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal defaultWlFee;

    public String getFromCity() {
        return fromCity == null ? "" : fromCity;
    }
    public String getToCity() {
        return toCity == null ? "" : toCity;
    }
    public BigDecimal getDefaultWlFee() {
        return defaultWlFee == null ? BigDecimal.ZERO : defaultWlFee;
    }
}
