package com.cjyc.common.model.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjyc.common.model.dto.BaseCityDto;
import com.cjyc.common.model.entity.City;
import com.cjyc.common.model.vo.web.city.CityTreeVo;
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

    /**
     * 根据关键字获取省/城市集合
     * @param name
     * @return
     */
    List<City> getCityTreeByKeyword(@Param("name") String name);
}

