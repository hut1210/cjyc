package com.cjyc.customer.api.service;

import com.cjyc.common.model.dto.promote.AdminPromoteDetailAddDto;
import com.cjyc.common.model.entity.AdminPromoteDetail;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 业务员推广明细 服务类
 * </p>
 *
 * @author JPG
 * @since 2019-11-13
 */
public interface IAdminPromoteDetailService extends IService<AdminPromoteDetail> {

    /**
     * 功能描述: 新增分析注册用户
     * @author liuxingxiang
     * @date 2019/11/13
     * @param dto
     * @return boolean
     */
    boolean add(AdminPromoteDetailAddDto dto);
}
