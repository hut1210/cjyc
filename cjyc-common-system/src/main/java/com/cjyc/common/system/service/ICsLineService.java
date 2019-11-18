package com.cjyc.common.system.service;

import com.cjyc.common.model.entity.Line;

public interface ICsLineService {
    Line getLineByCity(String startCityCode, String endCityCode, boolean isSearchCache);
}
