package com.cjyc.common.service.service;

import com.cjyc.common.model.entity.City;
import com.cjyc.common.model.vo.CityVo;

import java.util.List;
import java.util.Map;

/**
 * @auther litan
 * @description: com.cjyc.common.service.service
 * @date:2019/10/12
 */
public interface ICityService {

    List<CityVo> getCityList(String cityCode, String keyword);

    List<Map<String,Object>> getList(String cityCode);
}
