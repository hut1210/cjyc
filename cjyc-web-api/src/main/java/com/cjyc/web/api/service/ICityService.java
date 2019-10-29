package com.cjyc.web.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cjyc.common.model.dto.salesman.city.CityPageDto;
import com.cjyc.common.model.entity.City;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.dto.web.city.TreeCityDto;
import com.cjyc.common.model.vo.web.city.TreeCityVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 韵车城市信息表 服务类
 * </p>
 *
 * @author JPG
 * @since 2019-09-30
 */
public interface ICityService extends IService<City> {


    /**
     * 根据code查询城市
     * @author JPG
     * @since 2019/10/12 11:41
     * @param cityCode
     */
    City findById(@Param("cityCode") String cityCode);

    /**
     * 根据列查询城市列表
     * @author JPG
     * @since 2019/10/12 11:40
     * @param columnMap
     */
    List<City> selectList(Map<String, Object> columnMap);

    IPage<City> selectPage(CityPageDto cityPageDto);

    /**
     * 查询下属城市列表
     * @author JPG
     * @since 2019/10/12 13:31
     * @param code
     */
    List<City> selectChildList(String code);

    /**
     * 省城市树形结构
     * @return
     * @param treeCityDto
     */
    ResultVo<TreeCityVo> getTree(TreeCityDto treeCityDto);
}
