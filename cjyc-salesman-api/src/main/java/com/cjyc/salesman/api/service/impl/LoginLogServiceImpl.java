package com.cjyc.salesman.api.service.impl;

import com.cjyc.common.model.entity.LoginLog;
import com.cjyc.common.model.dao.ILoginLogDao;
import com.cjyc.salesman.api.service.ILoginLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 登录日志 服务实现类
 * </p>
 *
 * @author JPG
 * @since 2019-10-09
 */
@Service
public class LoginLogServiceImpl extends ServiceImpl<ILoginLogDao, LoginLog> implements ILoginLogService {

}
