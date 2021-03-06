package com.cjyc.common.model.vo.customer.city;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CityTreeVo implements Serializable {
    private static final long serialVersionUID = -7400163716561084589L;
     @ApiModelProperty("城市父编码")
     private String cityCode;

     @ApiModelProperty("城市名称")
     private String cityName;

    @ApiModelProperty("城市树")
    private List<AreaTreeVo> cityVos;
}