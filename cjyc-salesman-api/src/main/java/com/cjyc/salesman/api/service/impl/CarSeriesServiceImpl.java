package com.cjyc.salesman.api.service.impl;

import com.cjyc.common.model.entity.CarSeries;
import com.cjyc.common.model.dao.ICarSeriesDao;
import com.cjyc.salesman.api.service.ICarSeriesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 车系管理 服务实现类
 * </p>
 *
 * @author JPG
 * @since 2019-10-09
 */
@Service
public class CarSeriesServiceImpl extends ServiceImpl<ICarSeriesDao, CarSeries> implements ICarSeriesService {

}
