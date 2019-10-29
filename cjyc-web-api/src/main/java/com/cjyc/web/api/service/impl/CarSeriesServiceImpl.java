package com.cjyc.web.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.dao.ICarSeriesDao;
import com.cjyc.common.model.dto.web.carSeries.CarSeriesAddDto;
import com.cjyc.common.model.dto.web.carSeries.CarSeriesQueryDto;
import com.cjyc.common.model.dto.web.carSeries.CarSeriesUpdateDto;
import com.cjyc.common.model.entity.CarSeries;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.cjyc.common.model.util.StringUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.web.api.service.ICarSeriesService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description 品牌车系业务接口实现类
 * @Author LiuXingXiang
 * @Date 2019/10/28 16:03
 **/
@Service
public class CarSeriesServiceImpl extends ServiceImpl<ICarSeriesDao,CarSeries> implements ICarSeriesService {

    @Override
    public ResultVo add(CarSeriesAddDto carSeriesAddDto) {
        List<CarSeries> list = new ArrayList<>(10);
        String dtoModel = carSeriesAddDto.getModel();
        if (dtoModel.contains("，")) {
            dtoModel = dtoModel.replaceAll("，", ",");
        }
        String[] models = dtoModel.split(",");
        for (String model : models) {
            CarSeries carSeries = new CarSeries();
            carSeries.setCarCode(StringUtil.getUUID());
            carSeries.setBrand(carSeriesAddDto.getBrand());
            carSeries.setModel(model);
            carSeries.setLogoImg(StringUtil.getCarLogoURL(carSeriesAddDto.getBrand()));
            carSeries.setCreateTime(LocalDateTimeUtil.getMillisByLDT(LocalDateTime.now()));
            carSeries.setCreateUserId(carSeriesAddDto.getCreateUserId());
            list.add(carSeries);
        }
        boolean result = super.saveBatch(list);
        return result ? BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg())
                : BaseResultUtil.getVo(ResultEnum.FAIL.getCode(),ResultEnum.FAIL.getMsg());
    }

    @Override
    public ResultVo modify(CarSeriesUpdateDto carSeriesUpdateDto) {
        CarSeries carSeries = new CarSeries();
        BeanUtils.copyProperties(carSeriesUpdateDto,carSeries);
        carSeries.setUpdateTime(LocalDateTimeUtil.getMillisByLDT(LocalDateTime.now()));
        boolean result = super.updateById(carSeries);
        return result ? BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg())
                : BaseResultUtil.getVo(ResultEnum.FAIL.getCode(),ResultEnum.FAIL.getMsg());
    }

    @Override
    public ResultVo queryPage(CarSeriesQueryDto carSeriesQueryDto) {
        PageHelper.startPage(carSeriesQueryDto.getCurrentPage(), carSeriesQueryDto.getPageSize(), true);
        LambdaQueryWrapper<CarSeries> queryWrapper = new QueryWrapper<CarSeries>().lambda()
                .eq(!StringUtils.isEmpty(carSeriesQueryDto.getBrand()),CarSeries::getBrand,carSeriesQueryDto.getBrand())
                .eq(!StringUtils.isEmpty(carSeriesQueryDto.getModel()),CarSeries::getModel,carSeriesQueryDto.getModel());
        List<CarSeries> list = super.list(queryWrapper);
        PageInfo<CarSeries> pageInfo = new PageInfo<>(list);
        if (carSeriesQueryDto.getCurrentPage() > pageInfo.getPages()) {
            pageInfo.setList(null);
        }
        return BaseResultUtil.success(pageInfo);
    }
}
