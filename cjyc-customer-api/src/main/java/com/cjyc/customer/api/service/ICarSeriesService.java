package com.cjyc.customer.api.service;

import com.cjyc.common.model.dto.KeywordDto;
import com.cjyc.common.model.vo.ResultVo;

public interface ICarSeriesService {

    /**
     * 根据关键字查询品牌车系
     * @param dto
     * @return
     */
    ResultVo queryCarSeries(KeywordDto dto);
}
