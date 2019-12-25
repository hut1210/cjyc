package com.cjyc.web.api.service.impl;

import com.cjkj.common.model.PageData;
import com.cjkj.common.model.ResultData;
import com.cjkj.common.model.ReturnMsg;
import com.cjkj.usercenter.dto.yc.SelectPageUsersByDeptReq;
import com.cjkj.usercenter.dto.yc.SelectUsersByRoleResp;
import com.cjyc.common.model.dao.IWaybillCarDao;
import com.cjyc.common.model.dto.web.mineStore.ListMineSalesmanDto;
import com.cjyc.common.model.dto.web.mineStore.SetContactPersonDto;
import com.cjyc.common.model.dto.web.mineStore.StorageCarQueryDto;
import com.cjyc.common.model.entity.Admin;
import com.cjyc.common.model.entity.Store;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.mineStore.MySalesmanVo;
import com.cjyc.common.model.vo.web.mineStore.StorageCarVo;
import com.cjyc.common.system.feign.ISysUserService;
import com.cjyc.web.api.service.IAdminService;
import com.cjyc.web.api.service.IMineStoreService;
import com.cjyc.web.api.service.IStoreService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的业务中心
 */
@Service
public class MineStoreServiceImpl implements IMineStoreService {
    /**
     * 默认当前页码
     */
    private final static Integer DEFAULT_PAGE_NUM = 1;
    /**
     * 默认页码大小
     */
    private final static Integer DEFAULT_PAGE_SIZE = 10;
    @Autowired
    private IStoreService storeService;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private IAdminService adminService;
    @Resource
    private IWaybillCarDao waybillCarDao;

    @Override
    public ResultVo<PageVo<MySalesmanVo>> listSalesman(ListMineSalesmanDto dto) {
        Store store = storeService.getById(dto.getStoreId());
        if (store == null) {
            return BaseResultUtil.fail("业务中心id：" + dto.getStoreId() + "未查询到业务中心信息");
        }
        if (store.getDeptId() == null || store.getDeptId() <= 0L) {
            return BaseResultUtil.fail("未查询到机构信息，请检查");
        }
        int pageSize = dto.getPageSize() == null? DEFAULT_PAGE_SIZE: dto.getPageSize();
        int pageNum = dto.getPageNum() == null? DEFAULT_PAGE_NUM: dto.getPageNum();
        Long contactAdminId = store.getContactAdminId();
        SelectPageUsersByDeptReq req = new SelectPageUsersByDeptReq();
        req.setDeptId(store.getDeptId());
        req.setAccount(dto.getPhone());
        req.setName(dto.getName());
        req.setPageNum(pageNum);
        req.setPageSize(pageSize);
        ResultData<PageData<SelectUsersByRoleResp>> pageUserRd = sysUserService.getPageUsersByDept(req);
        if (!isResultDataSuccess(pageUserRd)) {
            return BaseResultUtil.fail("查询用户列表信息错误，原因：" + pageUserRd.getMsg());
        }
        if (pageUserRd.getData() == null) {
            return BaseResultUtil.success(new PageInfo<MySalesmanVo>());
        }
        List<MySalesmanVo> rsList = new ArrayList<>();
        pageUserRd.getData().getList().forEach(u -> {
            MySalesmanVo vo = new MySalesmanVo();
            vo.setAccount(u.getAccount());
            vo.setName(u.getName());
            vo.setPhone(u.getAccount());
            vo.setRoles(u.getRoles());
            Admin admin = adminService.getByUserId(u.getUserId());
            if (admin == null || contactAdminId == null
                    || !contactAdminId.equals(admin.getId())) {
                vo.setContactPerson(false);
            }else {
                vo.setContactPerson(true);
            }
            if (admin != null) {
                vo.setId(admin.getId());
            }
            rsList.add(vo);
        });
        long totalRecords = pageUserRd.getData().getTotal();
        PageVo<MySalesmanVo> page = PageVo.<MySalesmanVo>builder()
                .currentPage(pageNum)
                .pageSize(pageSize)
                .totalPages((int)totalRecords % pageSize == 0
                        ? (int)totalRecords/pageSize: (int)totalRecords/pageSize+1)
                .list(rsList)
                .totalRecords(totalRecords)
                .build();
        return BaseResultUtil.success(page);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultVo setContractPerson(SetContactPersonDto dto) {
        Store store = storeService.getById(dto.getStoreId());
        if (null == store) {
            return BaseResultUtil.fail("根据业务中心标识：" + dto.getStoreId() + "未查询到业务中心信息");
        }
        store.setContactAdminId(dto.getContactAdminId());
        storeService.updateById(store);
        return BaseResultUtil.success();
    }

    @Override
    public ResultVo getStorageCarPage(StorageCarQueryDto dto) {
        // 查询在库列表
        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        List<StorageCarVo> list = waybillCarDao.selectStorageCarPage(dto);
        PageInfo<List<StorageCarVo>> pageInfo = new PageInfo(list);
        // 查询在库数量
        Integer storageCarCount = getStorageCarCount(dto.getNowStoreId());
        Map<String,Object> map = new HashMap<>(1);
        map.put("count",storageCarCount);
        return BaseResultUtil.success(pageInfo,map);
    }

    public Integer getStorageCarCount(Long nowStoreId) {
        return waybillCarDao.selectStorageCount(nowStoreId);
    }


    /**
     * 验证ResultData状态是否正常
     * @param resultData
     * @return
     */
    private boolean isResultDataSuccess(ResultData resultData) {
        if (resultData == null) {
            return false;
        }
        if (!ReturnMsg.SUCCESS.getCode().equals(resultData.getCode())) {
            return false;
        }
        return true;
    }
}
