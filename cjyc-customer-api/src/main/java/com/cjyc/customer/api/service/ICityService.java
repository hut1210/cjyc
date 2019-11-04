package com.cjyc.customer.api.service;

import com.cjyc.common.model.dto.KeywordDto;
import com.cjyc.common.model.vo.ResultVo;

public interface ICityService {

    /**
     * 根据关键字查询城市
     * @param dto
     * @return
     */
    ResultVo queryCity(KeywordDto dto);
}
