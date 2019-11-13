package com.cjyc.customer.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.constant.FieldConstant;
import com.cjyc.common.model.dao.IAdminPromoteDetailDao;
import com.cjyc.common.model.dto.promote.AdminPromoteDetailAddDto;
import com.cjyc.common.model.entity.AdminPromoteDetail;
import com.cjyc.customer.api.service.IAdminPromoteDetailService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 业务员推广明细 服务实现类
 * </p>
 *
 * @author JPG
 * @since 2019-11-13
 */
@Service
public class AdminPromoteDetailServiceImpl extends ServiceImpl<IAdminPromoteDetailDao, AdminPromoteDetail> implements IAdminPromoteDetailService {

    @Override
    public boolean add(AdminPromoteDetailAddDto dto) {
        AdminPromoteDetail promoteDetail = new AdminPromoteDetail();
        BeanUtils.copyProperties(dto,promoteDetail);
        promoteDetail.setCreateTime(System.currentTimeMillis());
        promoteDetail.setState(FieldConstant.NOT_PLACE_ORDER);
        boolean result = super.save(promoteDetail);
        return result;
    }
}
