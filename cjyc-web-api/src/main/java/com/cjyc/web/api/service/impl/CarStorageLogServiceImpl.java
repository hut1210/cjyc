package com.cjyc.web.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.dao.ICarStorageLogDao;
import com.cjyc.common.model.entity.CarStorageLog;
import com.cjyc.web.api.service.ICarStorageLogService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 出入库记录 服务实现类
 * </p>
 *
 * @author JPG
 * @since 2019-11-22
 */
@Service
public class CarStorageLogServiceImpl extends ServiceImpl<ICarStorageLogDao, CarStorageLog> implements ICarStorageLogService {

}
