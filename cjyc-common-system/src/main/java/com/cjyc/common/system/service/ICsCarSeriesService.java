package com.cjyc.common.system.service;

import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.carSeries.CarSeriesTree;

import java.util.List;

public interface ICsCarSeriesService {

    /**
     * 查询车品车系
     * @param isSearchCache
     * @param keyword
     * @return
     */
    ResultVo<List<CarSeriesTree>> tree(boolean isSearchCache, String keyword);
}
