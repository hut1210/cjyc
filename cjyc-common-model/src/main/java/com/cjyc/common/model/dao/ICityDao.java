package com.cjyc.common.model.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjyc.common.model.dto.web.city.CityQueryDto;
import com.cjyc.common.model.dto.web.city.StoreDto;
import com.cjyc.common.model.entity.City;
import com.cjyc.common.model.entity.defined.FullCity;
import com.cjyc.common.model.vo.CityTreeVo;
import com.cjyc.common.model.vo.customer.city.HotCityVo;
import com.cjyc.common.model.vo.customer.city.ProvinceTreeVo;
import com.cjyc.common.model.vo.web.city.ProvinceCityVo;
import com.cjyc.common.model.vo.web.city.TreeCityVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

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

    FullCity findFullCityVo(String areaCode);

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

    /**
     * 获取热门城市
     * @return
     */
    List<HotCityVo> getHotCity();

    /**
     * 根据关键字查询城市树形结构
     * @param keyword
     * @return
     */
    List<ProvinceTreeVo> findThreeCity(@Param("keyword") String keyword,@Param("code") String code);

    /**
     * 查询多级城市实体
     * @author JPG
     * @since 2019/11/5 9:52
     * @param areaCode
     */
    FullCity find5LevelFullCity(String areaCode);
    FullCity find4LevelFullCity(String areaCode);
    FullCity findFullCity(String areaCode);
    FullCity find2LevelFullCity(String areaCode);

    /**
     * 功能描述: 分页查询城市信息
     * @author liuxingxiang
     * @date 2019/11/6
     * @param dto
     * @return java.util.List<com.cjyc.common.model.entity.defined.FullCity>
     */
    List<FullCity> selectCityList(CityQueryDto dto);

    /**
     * 功能描述: 根据业务中心ID查询当前业务中心覆盖区
     * @author liuxingxiang
     * @date 2019/11/6
     * @param dto
     * @return java.util.List<com.cjyc.common.model.entity.defined.FullCity>
     */
    List<FullCity> selectStoreAreaList(StoreDto dto);

    FullCity findFullCityByCityCode(String cityCode);

    /**
     * 功能描述: 查询业务中心查询已覆盖区列表
     * @author liuxingxiang
     * @date 2019/12/2
     * @param dto
     * @return java.util.List<com.cjyc.common.model.entity.defined.FullCity>
     */
    List<FullCity> selectCoveredList(StoreDto dto);

    /**
     * 功能描述: 查询业务中心查询未覆盖区列表
     * @author liuxingxiang
     * @date 2019/12/2
     * @param dto
     * @return java.util.List<com.cjyc.common.model.entity.defined.FullCity>
     */
    List<FullCity> selectNoCoveredList(StoreDto dto);
}

