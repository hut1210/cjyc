package com.cjyc.web.api.service.impl;

import com.cjyc.common.model.dto.web.salesman.TypeSalesmanDto;
import com.cjyc.common.model.entity.Admin;
import com.cjyc.common.model.dao.IAdminDao;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.admin.TypeSalesmanVo;
import com.cjyc.web.api.service.IAdminService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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

    @Override
    public ResultVo deliverySalesman(TypeSalesmanDto dto) {
        PageHelper.startPage(dto.getCurrentPage(), dto.getPageSize());
        List<TypeSalesmanVo> salesmanVos = adminDao.deliverySalesman(dto);
        PageInfo<TypeSalesmanVo> pageInfo =  new PageInfo<>(salesmanVos);
        return BaseResultUtil.success(pageInfo);
    }

}
