package com.cjyc.web.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cjkj.common.model.ResultData;
import com.cjkj.common.model.ReturnMsg;
import com.cjkj.common.utils.ExcelUtil;
import com.cjkj.usercenter.dto.common.AddDeptReq;
import com.cjkj.usercenter.dto.common.AddDeptResp;
import com.cjkj.usercenter.dto.common.SelectDeptResp;
import com.cjkj.usercenter.dto.common.UpdateDeptReq;
import com.cjkj.usercenter.dto.yc.SelectUsersByRoleResp;
import com.cjyc.common.model.dao.IAdminDao;
import com.cjyc.common.model.dto.web.carSeries.CarSeriesQueryDto;
import com.cjyc.common.model.dto.web.store.StoreAddDto;
import com.cjyc.common.model.dto.web.store.StoreQueryDto;
import com.cjyc.common.model.dto.web.store.StoreUpdateDto;
import com.cjyc.common.model.entity.Admin;
import com.cjyc.common.model.entity.CarSeries;
import com.cjyc.common.model.entity.Store;
import com.cjyc.common.model.dao.IStoreDao;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.enums.CommonStateEnum;
import com.cjyc.common.model.util.BasePageUtil;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.store.StoreExportExcel;
import com.cjyc.common.model.vo.web.carSeries.CarSeriesExportExcel;
import com.cjyc.web.api.feign.ISysDeptService;
import com.cjyc.web.api.feign.ISysUserService;
import com.cjyc.web.api.service.IStoreService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 韵车业务中心信息表 服务实现类
 * </p>
 *
 * @author JPG
 * @since 2019-10-23
 */
@Service
public class StoreServiceImpl extends ServiceImpl<IStoreDao, Store> implements IStoreService {
    @Resource
    private IStoreDao storeDao;

    @Autowired
    private ISysDeptService sysDeptService;

    @Autowired
    private ISysUserService sysUserService;

    @Resource
    private IAdminDao adminDao;

    @Override
    public List<Store> getByCityCode(String cityCode) {
        return storeDao.findByCityCode(cityCode);
    }

    @Override
    public List<Store> getByAreaCode(String areaCode) {
        return storeDao.findByAreaCode(areaCode);
    }

    @Override
    public ResultVo queryPage(StoreQueryDto dto) {
        BasePageUtil.initPage(dto);
        List<Store> list = getStoreList(dto);
        PageInfo<Store> pageInfo = new PageInfo<>(list);
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public boolean add(StoreAddDto storeAddDto) {
        Store store = new Store();
        BeanUtils.copyProperties(storeAddDto,store);
        store.setState(CommonStateEnum.WAIT_CHECK.code);
        store.setCreateTime(System.currentTimeMillis());
        //将业务中心信息添加到物流平台
        ResultData<Long> saveRd = addBizCenterToPlatform(store);
        if (!ReturnMsg.SUCCESS.getCode().equals(saveRd.getCode())) {
            log.error("保存业务中心失败，原因：" + saveRd.getMsg());
            return false;
        }
        store.setDeptId(saveRd.getData());
        return super.save(store);
    }

    @Override
    public boolean modify(StoreUpdateDto storeUpdateDto) {
        Store store = new Store();
        BeanUtils.copyProperties(storeUpdateDto,store);
        store.setUpdateTime(System.currentTimeMillis());
        //修改业务中心信息
        ResultData rd = updateBizCenterToPlatform(store);
        if (!ReturnMsg.SUCCESS.getCode().equals(rd.getCode())) {
            log.error("修改业务中心失败，原因：" + rd.getMsg());
            return false;
        }
        return super.updateById(store);
    }

    @Override
    public void exportExcel(HttpServletRequest request, HttpServletResponse response) {
        // 获取参数
        StoreQueryDto storeQueryDto = getStoreQueryDto(request);
        // 查询列表
        List<Store> storeList = getStoreList(storeQueryDto);

        if (!CollectionUtils.isEmpty(storeList)) {
            // 生成导出数据
            List<StoreExportExcel> exportExcelList = new ArrayList<>(10);
            for (Store store : storeList) {
                StoreExportExcel storeExportExcel = new StoreExportExcel();
                BeanUtils.copyProperties(store,storeExportExcel);
                exportExcelList.add(storeExportExcel);
            }
            String title = "业务中心";
            String sheetName = "业务中心";
            String fileName = "业务中心表.xls";
            try {
                ExcelUtil.exportExcel(exportExcelList, title, sheetName, StoreExportExcel.class, fileName, response);
            } catch (IOException e) {
                log.error("导出业务中心异常:{}",e);
            }
        }
    }

    @Override
    public ResultVo<List<Admin>> listAdminsByStoreId(Long storeId) {
        //业务中心列表获取
        Store store = baseMapper.selectById(storeId);
        if (null == store) {
            return BaseResultUtil.fail("业务中心获取错误, 根据id：" + storeId + "未获取到信息");
        }
        ResultData<List<SelectUsersByRoleResp>> usersRd =
                sysUserService.getUsersByDeptId(store.getDeptId());
        if (!ReturnMsg.SUCCESS.getCode().equals(usersRd.getCode())) {
            return BaseResultUtil.fail("查询用户列表信息失败，原因：" + usersRd.getMsg());
        }
        if (CollectionUtils.isEmpty(usersRd.getData())) {
            return BaseResultUtil.success();
        }
        List<Admin> adminList = new ArrayList<>();
        usersRd.getData().stream().forEach(u -> {
            Admin admin = adminDao.selectOne(new QueryWrapper<Admin>()
                    .eq("phone", u.getAccount()));
            if (null != admin) {
                admin.setBizDesc(u.getRoles());
                adminList.add(admin);
            }
        });
        return BaseResultUtil.success(adminList);
    }

    private StoreQueryDto getStoreQueryDto(HttpServletRequest request) {
        StoreQueryDto storeQueryDto = new StoreQueryDto();
        storeQueryDto.setCurrentPage(Integer.valueOf(request.getParameter("currentPage")));
        storeQueryDto.setPageSize(Integer.valueOf(request.getParameter("pageSize")));
        storeQueryDto.setName(request.getParameter("name"));
        storeQueryDto.setProvinceCode(request.getParameter("provinceCode"));
        storeQueryDto.setCityCode(request.getParameter("cityCode"));
        storeQueryDto.setAreaCode(request.getParameter("areaCode"));
        return storeQueryDto;
    }

    private List<Store> getStoreList(StoreQueryDto storeQueryDto) {
        PageHelper.startPage(storeQueryDto.getCurrentPage(), storeQueryDto.getPageSize(), true);
        LambdaQueryWrapper<Store> queryWrapper = new QueryWrapper<Store>().lambda()
                .eq(!StringUtils.isEmpty(storeQueryDto.getProvinceCode()),Store::getProvinceCode,storeQueryDto.getProvinceCode())
                .eq(!StringUtils.isEmpty(storeQueryDto.getCityCode()),Store::getCityCode,storeQueryDto.getCityCode())
                .eq(!StringUtils.isEmpty(storeQueryDto.getAreaCode()),Store::getAreaCode,storeQueryDto.getAreaCode())
                .like(!StringUtils.isEmpty(storeQueryDto.getName()),Store::getName,storeQueryDto.getName());
        return super.list(queryWrapper);
    }

    /**
     * 将业务中心添加到物流平台
     * @param store
     * @return
     */
    private ResultData<Long> addBizCenterToPlatform(Store store) {
        ResultData<SelectDeptResp> deptRd = sysDeptService.getDeptByCityCode(store.getCityCode());
        if (!ReturnMsg.SUCCESS.getCode().equals(deptRd.getCode())) {
            return ResultData.failed("根据城市编码查询机构信息错误，原因：" + deptRd.getMsg());
        }
        Long parentId = deptRd.getData().getDeptId();
        AddDeptReq deptReq = new AddDeptReq();
        deptReq.setName(store.getName());
        deptReq.setParentId(parentId);
        ResultData<AddDeptResp> saveRd = sysDeptService.save(deptReq);
        if (!ReturnMsg.SUCCESS.getCode().equals(saveRd.getCode())) {
            return ResultData.failed("保存业务中心失败，原因：" + saveRd.getMsg());
        }
        return ResultData.ok(saveRd.getData().getDeptId());
    }

    /**
     * 修改业务中心信息
     * @param store
     * @return
     */
    private ResultData updateBizCenterToPlatform(Store store) {
        //1.验证cityCode是否变更
        Store originalStore = storeDao.selectById(store.getId());
        if (null == originalStore) {
            return ResultData.failed("根据业务中心id：" + store.getId() + ", 未查询到机构信息");
        }
        Long parentDeptId = null;
        if (!store.getCityCode().equals(originalStore.getCityCode())) {
            //上级机构变更，物流平台需要变更部门
            ResultData<SelectDeptResp> deptRd =
                    sysDeptService.getDeptByCityCode(store.getCityCode());
            if (!ReturnMsg.SUCCESS.getCode().equals(deptRd.getCode())) {
                return ResultData.failed("查询机构信息错误，原因: " + deptRd.getMsg());
            }
            parentDeptId = deptRd.getData().getDeptId();
        }
        UpdateDeptReq deptReq = new UpdateDeptReq();
        deptReq.setDeptId(originalStore.getDeptId());
        deptReq.setName(store.getName());
        deptReq.setParentId(parentDeptId);
        return sysDeptService.update(deptReq);
    }
}
