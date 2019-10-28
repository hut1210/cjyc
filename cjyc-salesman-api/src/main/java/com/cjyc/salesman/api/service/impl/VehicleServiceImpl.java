package com.cjyc.salesman.api.service.impl;

import com.cjyc.common.model.entity.Vehicle;
import com.cjyc.common.model.dao.IVehicleDao;
import com.cjyc.salesman.api.service.IVehicleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 运输车辆表 服务实现类
 * </p>
 *
 * @author JPG
 * @since 2019-10-09
 */
@Service
public class VehicleServiceImpl extends ServiceImpl<IVehicleDao, Vehicle> implements IVehicleService {

}
