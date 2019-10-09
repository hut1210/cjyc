package com.cjyc.salesman.api.service.impl;

import com.cjyc.common.model.entity.Incrementer;
import com.cjyc.common.model.dao.IIncrementerDao;
import com.cjyc.salesman.api.service.IIncrementerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单、任务编号自增表 服务实现类
 * </p>
 *
 * @author JPG
 * @since 2019-10-09
 */
@Service
public class IncrementerServiceImpl extends ServiceImpl<IIncrementerDao, Incrementer> implements IIncrementerService {

}
