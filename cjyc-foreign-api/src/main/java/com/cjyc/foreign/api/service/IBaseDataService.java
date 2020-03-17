package com.cjyc.foreign.api.service;

import com.cjyc.common.model.entity.CarSeries;
import com.cjyc.common.model.entity.City;

import java.util.List;

/**
 * @author zcm
 * @date 2020/3/17 14:56
 */
public interface IBaseDataService {
    List<City> getAllCity();

    List<CarSeries> getAllCarSeries();
}
