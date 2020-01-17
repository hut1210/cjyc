package com.cjyc.salesman.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cjkj.common.model.PageData;
import com.cjkj.common.model.ResultData;
import com.cjkj.usercenter.dto.yc.SelectPageUsersForSalesmanReq;
import com.cjkj.usercenter.dto.yc.SelectUsersByRoleResp;
import com.cjyc.common.model.dao.IAdminDao;
import com.cjyc.common.model.dao.IOrderDao;
import com.cjyc.common.model.dao.IStoreDao;
import com.cjyc.common.model.dto.salesman.order.SalesmanQueryDto;
import com.cjyc.common.model.dto.web.salesman.AdminPageNewDto;
import com.cjyc.common.model.entity.Admin;
import com.cjyc.common.model.entity.Order;
import com.cjyc.common.model.entity.Store;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.web.admin.AdminPageVo;
import com.cjyc.common.system.feign.ISysUserService;
import com.cjyc.common.system.util.ResultDataUtil;
import com.cjyc.salesman.api.service.IAdminService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 业务员Service
 */
@Service
public class AdminServiceImpl implements IAdminService {
    @Resource
    private IStoreDao storeDao;
    @Resource
    private ISysUserService sysUserService;
    @Resource
    private IAdminDao adminDao;

    @Override
    public PageVo<AdminPageVo> listPage(SalesmanQueryDto dto) {
        Store store = storeDao.selectById(dto.getInputStoreId());
        if (null == store || store.getDeptId() == null || store.getDeptId() <= 0L) {
            return PageVo.<AdminPageVo>builder()
                    .totalRecords(0)
                    .totalPages(0)
                    .pageSize(dto.getPageSize())
                    .currentPage(dto.getCurrentPage())
                    .list(new ArrayList<AdminPageVo>())
                    .build();
        }
        List<AdminPageVo> resList = new ArrayList<>();
        SelectPageUsersForSalesmanReq req = new SelectPageUsersForSalesmanReq();
        req.setDeptIdList(Arrays.asList(store.getDeptId()));
        if (!StringUtils.isEmpty(dto.getSearchValue())) {
            req.setName(dto.getSearchValue());
        }
        ResultData<PageData<SelectUsersByRoleResp>> rd = sysUserService.getPageUsersForSalesmanApp(req);
        if (ResultDataUtil.isSuccess(rd)) {
            if(rd == null || rd.getData() == null || CollectionUtils.isEmpty(rd.getData().getList())){
                return PageVo.<AdminPageVo>builder()
                        .totalRecords(0)
                        .totalPages(0)
                        .pageSize(dto.getPageSize())
                        .currentPage(dto.getCurrentPage())
                        .list(new ArrayList<AdminPageVo>())
                        .build();
            }
            List<SelectUsersByRoleResp> list = rd.getData().getList();
            Set<Long> collect = list.stream().map(SelectUsersByRoleResp::getUserId).collect(Collectors.toSet());
            List<Admin> adminList = adminDao.findListByUserIds(collect);
            if(CollectionUtils.isEmpty(adminList)){
                return PageVo.<AdminPageVo>builder()
                        .totalRecords(0)
                        .totalPages(0)
                        .pageSize(dto.getPageSize())
                        .currentPage(dto.getCurrentPage())
                        .list(new ArrayList<AdminPageVo>())
                        .build();
            }
            //附加数据
            for (SelectUsersByRoleResp selectUsersByRoleResp : list) {
                AdminPageVo adminPageVo = new AdminPageVo();
                BeanUtils.copyProperties(selectUsersByRoleResp, adminPageVo);
                for (Admin admin : adminList) {
                    if(admin.getUserId().equals(selectUsersByRoleResp.getUserId())){
                        adminPageVo.setBizDesc(admin.getBizDesc());
                        adminPageVo.setState(admin.getState());
                        adminPageVo.setCreateTime(admin.getCreateTime());
                        adminPageVo.setCreateUser(admin.getCreateUser());
                        adminPageVo.setId(admin.getId());
                    }
                }
                resList.add(adminPageVo);
            }

            return PageVo.<AdminPageVo>builder()
                    .totalRecords(rd.getData().getTotal())
                    .pageSize(dto.getPageSize())
                    .currentPage(dto.getCurrentPage())
                    .totalPages(getPages(rd.getData().getTotal(), dto.getPageSize()))
                    .list(resList)
                    .build();
        }else {
            return PageVo.<AdminPageVo>builder()
                    .totalRecords(0)
                    .totalPages(0)
                    .pageSize(dto.getPageSize())
                    .currentPage(dto.getCurrentPage())
                    .list(new ArrayList<AdminPageVo>())
                    .build();
        }
    }

    @Override
    public PageVo<AdminPageVo> listPageNew(SalesmanQueryDto dto) {
        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        List<AdminPageVo> pageList = adminDao.getPageListForSalesmanApp(dto);
        int totalRecords = CollectionUtils.isEmpty(pageList)?0: pageList.size();
        return PageVo.<AdminPageVo>builder()
                .totalRecords(totalRecords)
                .totalPages(getPages(totalRecords, dto.getPageSize()))
                .pageSize(dto.getPageSize())
                .currentPage(dto.getCurrentPage())
                .list(pageList)
                .build();
    }

    private int getPages(long total, Integer pageSize) {
        if(total <= 0){
            return 0;
        }
        return new BigDecimal(total).divide(new BigDecimal(pageSize), BigDecimal.ROUND_CEILING).intValue();
    }
}
