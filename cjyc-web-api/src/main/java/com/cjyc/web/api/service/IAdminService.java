package com.cjyc.web.api.service;

import com.cjyc.common.model.dto.web.salesman.TypeSalesmanDto;
import com.cjyc.common.model.entity.Admin;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cjyc.common.model.vo.ResultVo;

import java.util.List;

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

}
