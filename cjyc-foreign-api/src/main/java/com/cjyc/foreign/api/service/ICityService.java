package com.cjyc.foreign.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.foreign.api.dto.res.CityResDto;
import com.cjyc.foreign.api.entity.City;

import java.util.List;

/**
 * @Description 城市业务接口
 Author Liu Xing Xiang
 * @Date 2020/3/10 14:51
 **/
public interface ICityService extends IService<City> {
    /**
     * 功能描述: 查询所有城市
     * @author liuxingxiang
     * @date 2020/3/10
     * @param
     * @return com.cjyc.common.model.vo.ResultVo<com.cjyc.foreign.api.dto.res.CityResDto>
     */
    ResultVo<List<CityResDto>> getAllCity();
}
