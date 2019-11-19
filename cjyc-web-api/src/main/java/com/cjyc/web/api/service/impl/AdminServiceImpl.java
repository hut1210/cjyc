package com.cjyc.web.api.service.impl;

import com.cjkj.common.model.ResultData;
import com.cjkj.common.model.ReturnMsg;
import com.cjkj.usercenter.dto.common.SelectRoleResp;
import com.cjkj.usercenter.dto.yc.SelectUsersByRoleResp;
import com.cjyc.common.model.dto.web.salesman.AdminPageDto;
import com.cjyc.common.model.dto.web.salesman.TypeSalesmanDto;
import com.cjyc.common.model.entity.Admin;
import com.cjyc.common.model.dao.IAdminDao;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.entity.Store;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.admin.AdminPageVo;
import com.cjyc.common.model.vo.web.admin.TypeSalesmanVo;
import com.cjyc.common.system.feign.ISysDeptService;
import com.cjyc.common.system.feign.ISysRoleService;
import com.cjyc.common.system.service.ICsStoreService;
import com.cjyc.web.api.service.IAdminService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestHeader;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    @Resource
    private ISysRoleService sysRoleService;
    @Resource
    private ISysDeptService deptService;
    @Resource
    private ICsStoreService csStoreService;

    @Override
    public Admin getByUserId(Long userId) {
        return adminDao.findByUserId(userId);
    }

    @Override
    public ResultVo deliverySalesman(TypeSalesmanDto dto) {
        PageHelper.startPage(dto.getCurrentPage(), dto.getPageSize());
        List<TypeSalesmanVo> salesmanVos = adminDao.deliverySalesman(dto);
        if(dto.getIsPage() == 0){
            //不分页
            return BaseResultUtil.success(salesmanVos);
        }
        PageInfo<TypeSalesmanVo> pageInfo =  new PageInfo<>(salesmanVos);
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public List<AdminPageVo> page(AdminPageDto paramsDto) {
        //计算业务中心
        Long deptId = null;
        if(paramsDto.getStoreId() == null || paramsDto.getStoreId() == 0){
            //查询机构
            ResultData<SelectRoleResp> resultData = sysRoleService.getById(paramsDto.getRoleId());
            if(resultData == null || resultData.getData() == null){
                return null;
            }
            deptId = resultData.getData().getDeptId();

        }else{
            Store store = csStoreService.getById(paramsDto.getStoreId(), true);
            deptId = store.getDeptId();
        }

/*        //查询部门下所有业务员userID
        ResultData<List<SelectUsersByRoleResp>> usersData = deptService.getUsersByDeptId(deptId);
        if(usersData == null || CollectionUtils.isEmpty(usersData.getData())){
            return null;
        }
        Set<Long> collect = usersData.getData().stream().map(SelectUsersByRoleResp::getUserId).collect(Collectors.toSet());

        PageHelper.startPage(paramsDto.getCurrentPage(), paramsDto.getPageSize(), true);
        List<AdminPageVo>  list = adminDao.findList(paramsDto, collect);
        PageInfo<AdminPageVo> pageInfo =
        return ;*/
        return null;
    }

}
