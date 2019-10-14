package com.cjyc.common.model.dao;

import com.cjyc.common.model.entity.CarSeries;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

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
}
