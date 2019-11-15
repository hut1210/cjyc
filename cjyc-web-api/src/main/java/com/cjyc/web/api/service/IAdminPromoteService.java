package com.cjyc.web.api.service;

import com.cjyc.common.model.dto.promote.AdminPromoteQueryDto;
import com.cjyc.common.model.entity.AdminPromote;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cjyc.common.model.vo.ResultVo;

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
     * 功能描述: 查询分享分页列表
     * @author liuxingxiang
     * @date 2019/11/13
     * @param dto
     * @return com.cjyc.common.model.vo.ResultVo
     */
    ResultVo getPage(AdminPromoteQueryDto dto);
}
