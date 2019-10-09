package com.cjyc.salesman.api.service.impl;

import com.cjyc.common.model.entity.Carrier;
import com.cjyc.common.model.dao.ICarrierDao;
import com.cjyc.salesman.api.service.ICarrierService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 承运商信息表（个人也算承运商） 服务实现类
 * </p>
 *
 * @author JPG
 * @since 2019-10-09
 */
@Service
public class CarrierServiceImpl extends ServiceImpl<ICarrierDao, Carrier> implements ICarrierService {

}
