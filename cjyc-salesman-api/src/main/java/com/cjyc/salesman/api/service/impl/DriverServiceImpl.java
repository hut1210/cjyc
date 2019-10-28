package com.cjyc.salesman.api.service.impl;

import com.cjyc.common.model.entity.Driver;
import com.cjyc.common.model.dao.IDriverDao;
import com.cjyc.salesman.api.service.IDriverService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 司机信息表（登录司机端APP用户） 服务实现类
 * </p>
 *
 * @author JPG
 * @since 2019-10-09
 */
@Service
public class DriverServiceImpl extends ServiceImpl<IDriverDao, Driver> implements IDriverService {

}
