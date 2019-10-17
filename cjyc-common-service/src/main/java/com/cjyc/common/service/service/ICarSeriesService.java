package com.cjyc.common.service.service;

import com.cjyc.common.model.entity.CarSeries;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @auther litan
 * @description: com.cjyc.common.service.service
 * @date:2019/10/12
 */
public interface ICarSeriesService {

    List<CarSeries> getList(String keyword);

    List<String> getBrand();

    List<String> getSeriesByBrand(String brand);
}
