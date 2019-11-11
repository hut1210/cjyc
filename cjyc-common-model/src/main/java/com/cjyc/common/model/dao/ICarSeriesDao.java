package com.cjyc.common.model.dao;

import com.cjyc.common.model.entity.CarSeries;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjyc.common.model.vo.web.carSeries.CarSeriesTree;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 车系管理 Mapper 接口
 * </p>
 *
 * @author JPG
 * @since 2019-09-29
 */
@Repository
public interface ICarSeriesDao extends BaseMapper<CarSeries> {

    /**
     * 通过品牌和型号查询logo
     * @param brand
     * @param model
     * @return
     */
    String getLogoImgByBraMod(@Param("brand") String brand,@Param("model") String model);

    List<String> getBrand();

    List<String> getSeriesByBrand(@Param("brand") String brand);

    /**
     * 根据关键字查询品牌车系
     * @param keyword
     * @return
     */
    List<CarSeriesTree> findTree(@Param("keyword") String keyword);
}
