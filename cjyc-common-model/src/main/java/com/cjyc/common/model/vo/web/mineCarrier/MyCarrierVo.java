package com.cjyc.common.model.vo.web.mineCarrier;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class MyCarrierVo implements Serializable {
    private static final long serialVersionUID = 6946362184671817538L;
    @ApiModelProperty(value = "承运商id")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "承运商名称")
    private String name;
}