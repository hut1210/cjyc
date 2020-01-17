package com.cjyc.salesman.api.service;

import com.cjyc.common.model.dto.salesman.order.SalesmanQueryDto;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.web.admin.AdminPageVo;

/**
 * 业务员Service
 */
public interface IAdminService {
    /**
     * 分页查询
     * @param dto
     * @return
     */
    PageVo<AdminPageVo> listPage(SalesmanQueryDto dto);

    /**
     * 分页查询_改版
     * @param dto
     * @return
     */
    PageVo<AdminPageVo> listPageNew(SalesmanQueryDto dto);
}
