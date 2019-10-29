package com.cjyc.web.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cjyc.common.model.dto.web.carSeries.CarSeriesAddDto;
import com.cjyc.common.model.dto.web.carSeries.CarSeriesQueryDto;
import com.cjyc.common.model.dto.web.carSeries.CarSeriesUpdateDto;
import com.cjyc.common.model.entity.CarSeries;
import com.cjyc.common.model.vo.ResultVo;

/**
 * @Description 品牌车系业务接口
 * @Author LiuXingXiang
 * @Date 2019/10/28 16:01
 **/
public interface ICarSeriesService extends IService<CarSeries> {

    /**
     * 新增
     * @param carSeriesAddDto
     * @return
     */
    ResultVo add(CarSeriesAddDto carSeriesAddDto);

    /**
     * 修改
     * @param carSeriesUpdateDto
     * @return
     */
    ResultVo modify(CarSeriesUpdateDto carSeriesUpdateDto);

    /**
     * 分页查询
     * @param carSeriesQueryDto
     * @return
     */
    ResultVo queryPage(CarSeriesQueryDto carSeriesQueryDto);
}
