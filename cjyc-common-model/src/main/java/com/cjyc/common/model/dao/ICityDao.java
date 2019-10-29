package com.cjyc.common.model.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjyc.common.model.dto.BaseCityDto;
import com.cjyc.common.model.entity.City;
import com.cjyc.common.model.vo.web.city.ProvinceCityVo;
import com.cjyc.common.model.vo.web.city.TreeCityVo;
import io.swagger.annotations.ApiParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 韵车城市信息表 Mapper 接口
 * </p>
 *
 * @author JPG
 * @since 2019-09-29
 */
@Repository
public interface ICityDao extends BaseMapper<City> {

    City findById(@Param("cityCode") String cityCode);

    List<City> findList();

    List<City> findChildList(String code);

    List<Map<String,Object>> getList(@Param("cityCode") String cityCode);

    /**
     *  通过大区codes 获取区县codes
     * @param code
     * @return
     */
    List<String> getAreaCodesByLarCode(List<String> code);

    /**
     * 通过省/直辖市codes获取区县codes
     * @param code
     * @return
     */
    List<String> getAreaCodesByProCode(List<String> code);

    /**
     * 通过城市codes获取区县codes
     * @param code
     * @return
     */
    List<String> getAreaCodesByCityCode(List<String> code);

    /**
     * 根据编码获取该编码和名称
     * @param code
     * @return
     */
    List<BaseCityDto> getCityAndName(List<String> code);

    /**
     * 通过城市编码查询上一级
     * @param code
     * @return
     */
    ProvinceCityVo getProvinceCityByCode(@Param("code") String code);

    /**
     * 获取所有省/直辖市
     * @return
     */
    List<HashMap<String,String>> getAllProvince();

    /**
     * 获取下一级所有城市
     * @param code
     * @return
     */
    List<HashMap<String,String>> getAllCity(@ApiParam("code") String code);

    List<TreeCityVo> findListByLevel(Integer level);
}

