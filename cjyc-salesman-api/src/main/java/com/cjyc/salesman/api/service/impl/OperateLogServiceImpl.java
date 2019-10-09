package com.cjyc.salesman.api.service.impl;

import com.cjyc.common.model.entity.OperateLog;
import com.cjyc.common.model.dao.IOperateLogDao;
import com.cjyc.salesman.api.service.IOperateLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 操作日志 服务实现类
 * </p>
 *
 * @author JPG
 * @since 2019-10-09
 */
@Service
public class OperateLogServiceImpl extends ServiceImpl<IOperateLogDao, OperateLog> implements IOperateLogService {

}
