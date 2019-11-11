package com.cjyc.common.model.vo.customer.city;

import com.cjyc.common.model.dto.BaseCityDto;
import com.cjyc.common.model.vo.CityTreeVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CityVo implements Serializable {

    @ApiModelProperty("热门撑死")
    private List<HotCityVo> hotCityVos;

    @ApiModelProperty("城市树")
    private List<CityTreeVo> cityTreeVos;
}