package com.cjyc.common.model.entity;

import com.cjyc.common.model.dto.BaseCityDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class BusinessCityCode implements Serializable {

    @ApiModelProperty("全国code")
    private List<BaseCityDto> countryList;

    @ApiModelProperty("大区code")
    private List<BaseCityDto> largeAreaList;

    @ApiModelProperty("省/直辖市code")
    private List<BaseCityDto> provinceList;

    @ApiModelProperty("市code")
    private List<BaseCityDto> cityList;

    @ApiModelProperty("区/县code")
    private List<BaseCityDto> areaList;
}