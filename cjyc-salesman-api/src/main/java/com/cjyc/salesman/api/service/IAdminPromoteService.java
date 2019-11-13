package com.cjyc.salesman.api.service;

import com.cjyc.common.model.dto.promote.AdminPromoteAddDto;
import com.cjyc.common.model.entity.AdminPromote;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 业务员推广表 服务类
 * </p>
 *
 * @author JPG
 * @since 2019-11-13
 */
public interface IAdminPromoteService extends IService<AdminPromote> {

    /**
     * 功能描述: 新增分享信息
     * @author liuxingxiang
     * @date 2019/11/13
     * @param dto
     * @return boolean
     */
    boolean add(AdminPromoteAddDto dto);
}
