package com.cjyc.web.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cjyc.common.model.dto.web.salesman.AdminPageDto;
import com.cjyc.common.model.dto.web.salesman.AdminPageNewDto;
import com.cjyc.common.model.dto.web.salesman.TypeSalesmanDto;
import com.cjyc.common.model.entity.Admin;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.admin.AdminPageVo;
import com.cjyc.common.model.vo.web.admin.CacheData;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 韵车后台管理员表 服务类
 * </p>
 *
 * @author JPG
 * @since 2019-10-17
 */
public interface IAdminService extends IService<Admin> {

    Admin getByUserId(Long userId);

    ResultVo deliverySalesman(TypeSalesmanDto dto);

    ResultVo<PageVo<AdminPageVo>> page(AdminPageDto reqDto);

    CacheData getCacheData(Long userId, Long roleId);

    /************************************韵车集成改版 st***********************************/
    ResultVo<PageVo<AdminPageVo>> pageNew(AdminPageNewDto reqDto);
    CacheData getCacheDataNew(Long userId, Long roleId);

    ResultVo deliverySalesmanNew(TypeSalesmanDto dto);

    void exportAdminListExcel(HttpServletRequest request, HttpServletResponse response);
    /************************************韵车集成改版 ed***********************************/
}
