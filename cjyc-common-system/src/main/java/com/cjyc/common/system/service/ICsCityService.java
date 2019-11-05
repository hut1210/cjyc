package com.cjyc.common.system.service;

import com.cjyc.common.model.enums.city.CityLevelEnum;
import com.cjyc.common.model.vo.web.city.FullCity;

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
}
