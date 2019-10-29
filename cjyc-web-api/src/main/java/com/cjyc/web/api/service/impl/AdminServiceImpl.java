package com.cjyc.web.api.service.impl;

import com.cjyc.common.model.entity.Admin;
import com.cjyc.common.model.dao.IAdminDao;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.web.api.service.IAdminService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 韵车后台管理员表 服务实现类
 * </p>
 *
 * @author JPG
 * @since 2019-10-17
 */
@Service
public class AdminServiceImpl extends ServiceImpl<IAdminDao, Admin> implements IAdminService {
    @Resource
    private IAdminDao adminDao;

    @Override
    public Admin getByUserId(Long userId) {
        return adminDao.findByUserId(userId);
    }
}