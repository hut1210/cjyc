package com.cjyc.common.model.vo.customer.city;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CityVo implements Serializable {

    @ApiModelProperty("热门城市")
    private List<HotCityVo> hotCityVos;

    @ApiModelProperty("城市树")
    private List<ProvinceTreeVo> cityTreeVos;
}