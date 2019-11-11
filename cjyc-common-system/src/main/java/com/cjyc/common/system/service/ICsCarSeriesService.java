package com.cjyc.common.system.service;

import com.cjyc.common.model.vo.web.carSeries.CarSeriesTree;

import java.util.List;

public interface ICsCarSeriesService {
    List<CarSeriesTree> tree(boolean isSearchCache,String keyword);
}
