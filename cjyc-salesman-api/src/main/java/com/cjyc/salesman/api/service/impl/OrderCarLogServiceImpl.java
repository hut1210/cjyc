package com.cjyc.salesman.api.service.impl;

import com.cjyc.common.model.entity.OrderCarLog;
import com.cjyc.common.model.dao.IOrderCarLogDao;
import com.cjyc.salesman.api.service.IOrderCarLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 车辆物流轨迹 服务实现类
 * </p>
 *
 * @author JPG
 * @since 2019-10-09
 */
@Service
public class OrderCarLogServiceImpl extends ServiceImpl<IOrderCarLogDao, OrderCarLog> implements IOrderCarLogService {

}
