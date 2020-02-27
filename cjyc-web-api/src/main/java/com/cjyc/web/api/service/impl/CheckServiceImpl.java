package com.cjyc.web.api.service.impl;

import com.cjyc.common.model.entity.Check;
import com.cjyc.common.model.dao.ICheckDao;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.web.api.service.ICheckService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统审核表 服务实现类
 * </p>
 *
 * @author JPG
 * @since 2020-02-27
 */
@Service
public class CheckServiceImpl extends ServiceImpl<ICheckDao, Check> implements ICheckService {

}
