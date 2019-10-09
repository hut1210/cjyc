package com.cjyc.salesman.api.service.impl;

import com.cjyc.common.model.entity.Waybill;
import com.cjyc.common.model.dao.IWaybillDao;
import com.cjyc.salesman.api.service.IWaybillService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 运单表(业务员调度单) 服务实现类
 * </p>
 *
 * @author JPG
 * @since 2019-10-09
 */
@Service
public class WaybillServiceImpl extends ServiceImpl<IWaybillDao, Waybill> implements IWaybillService {

}
