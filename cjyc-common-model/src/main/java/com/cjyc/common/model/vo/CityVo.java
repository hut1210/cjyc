package com.cjyc.common.model.vo;

import com.cjyc.common.model.entity.City;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CityVo implements Serializable {

    private static final long serialVersionUID = 1L;

    List<City> cityList;

    List<City> hotCityList;


}
