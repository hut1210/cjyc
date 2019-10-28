package com.cjyc.web.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.dao.ICarSeriesDao;
import com.cjyc.common.model.dto.web.carSeries.CarSeriesAddDto;
import com.cjyc.common.model.entity.CarSeries;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.cjyc.common.model.util.StringUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.web.api.service.ICarSeriesService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.Arrays;
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
        List<CarSeries> list = Arrays.asList();
        String[] models = carSeriesAddDto.getModel().split(",");
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
}
