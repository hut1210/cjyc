package com.cjyc.common.model.vo.web.postal;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ProvinceVo implements Serializable {
    private static final long serialVersionUID = -2564356613510343608L;

    @ApiModelProperty(value = "省直辖市名称")
    private String provinceName;

    @ApiModelProperty(value = "地区")
    private List<AreaVo> areaVoList;
}