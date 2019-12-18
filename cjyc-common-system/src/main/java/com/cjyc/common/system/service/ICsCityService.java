package com.cjyc.common.system.service;

import com.cjyc.common.model.dto.KeywordDto;
import com.cjyc.common.model.dto.ThreeCityDto;
import com.cjyc.common.model.enums.city.CityLevelEnum;
import com.cjyc.common.model.entity.defined.FullCity;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.customer.city.CityVo;

/**
 * 城市公用业务类
 * @author JPG
 */
public interface ICsCityService {
    /**
     * 查询多级城市对象
     * @author JPG
     * @since 2019/11/5 9:33
     * @param areaCode
     * @param cityLevelEnum 根节点，默认3级
     */
    FullCity findFullCity(String areaCode, CityLevelEnum cityLevelEnum);

    FullCity findFullCityByCityCode(String cityCode);

    /**
     * 根据关键字查询城市(热门城市/三级城市)
     * @param dto
     * @return
     */
    ResultVo<CityVo> queryCity(KeywordDto dto);

    /**
     *  根据角色id或登录id获取三级城市列表
     * @param dto
     * @return
     */
    ResultVo<CityVo> findCityTree(ThreeCityDto dto);
}
