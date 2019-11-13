package com.cjyc.salesman.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.dao.IAdminPromoteDao;
import com.cjyc.common.model.dto.promote.AdminPromoteAddDto;
import com.cjyc.common.model.entity.AdminPromote;
import com.cjyc.salesman.api.service.IAdminPromoteService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 业务员推广表 服务实现类
 * </p>
 *
 * @author JPG
 * @since 2019-11-13
 */
@Service
public class AdminPromoteServiceImpl extends ServiceImpl<IAdminPromoteDao, AdminPromote> implements IAdminPromoteService {

    @Override
    public boolean add(AdminPromoteAddDto dto) {
        AdminPromote promote = new AdminPromote();
        BeanUtils.copyProperties(dto,promote);
        promote.setCreateTime(System.currentTimeMillis());
        boolean result = super.save(promote);
        return result;
    }
}
