package com.cjyc.common.model.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.mapper.Mapper;
import com.cjyc.common.model.entity.City;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 韵车城市信息表 Mapper 接口
 * </p>
 *
 * @author JPG
 * @since 2019-09-29
 */
public interface ICityDao extends BaseMapper<City> {

    City findById(@Param("cityCode") String cityCode);

    List<City> findList();

    List<City> findChildList(String code);
}

