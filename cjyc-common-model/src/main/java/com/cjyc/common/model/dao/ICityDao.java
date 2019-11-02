package com.cjyc.common.model.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjyc.common.model.dto.BaseCityDto;
import com.cjyc.common.model.entity.City;
import com.cjyc.common.model.vo.web.city.CityTreeVo;
import com.cjyc.common.model.vo.web.city.FullCityVo;
import com.cjyc.common.model.vo.web.city.ProvinceCityVo;
import com.cjyc.common.model.vo.web.city.TreeCityVo;
import io.swagger.annotations.ApiParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
     * 通过城市编码查询上一级
     * @param code
     * @return
     */
    ProvinceCityVo getProvinceCityByCode(@Param("code") String code);

    List<TreeCityVo> findListByLevel(Integer level);

    /**
     * 根据城市级别查询树形结构
     * @param startLevel
     * @param endLevel
     * @return
     */
    List<CityTreeVo> getAllByLevel(@Param("startLevel") Integer startLevel,@Param("endLevel")Integer endLevel);

    FullCityVo findFullCityVo(String areaCode);

    /**
     * 根据关键字获取省/城市集合
     * @param name
     * @return
     */
    List<City> getCityTreeByKeyword(@Param("name") String name);

    /**
     * 根据城市编码获取省市集合
     * @param codeSet
     * @return
     */
    List<CityTreeVo> getCityByCodes(@Param("codeSet") Set<String> codeSet);

    /**
     * 根据城市名称获取城市code
     * @param name
     * @return
     */
    String getCodeByName(@Param("name") String name);
}

