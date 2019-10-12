package com.cjyc.common.service.service;

import com.cjyc.common.model.entity.City;

import java.util.List;

/**
 * @auther litan
 * @description: com.cjyc.common.service.service
 * @date:2019/10/12
 */
public interface ICityService {

    List<City> getCityList(String cityCode, String keyword);
}
