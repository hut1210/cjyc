package com.cjyc.common.system.service.sys;

import com.cjkj.common.model.ResultData;
import com.cjkj.usercenter.dto.common.SelectDeptResp;
import com.cjkj.usercenter.dto.common.SelectRoleResp;
import com.cjyc.common.model.entity.Store;
import com.cjyc.common.model.entity.defined.BizScope;
import com.cjyc.common.model.enums.BizScopeEnum;
import com.cjyc.common.model.util.YmlProperty;
import com.cjyc.common.system.feign.ISysDeptService;
import com.cjyc.common.system.feign.ISysRoleService;
import com.cjyc.common.system.service.ICsStoreService;
import com.google.common.base.Joiner;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * fegin接口传参和返回值处理接口
 * @author JPG
 */
@Service
public class CsSysServiceImpl implements ICsSysService {

    private  static final String TOP_ADMIN_DEPT = YmlProperty.get("cjkj.dept_admin_id");

    @Resource
    private ISysRoleService sysRoleService;
    @Resource
    private ISysDeptService sysDeptService;
    @Resource
    private ICsStoreService csStoreService;

    /**
     * 获取角色业务范围: 0全国，-1无业务范围，StoreIds逗号分隔字符串
     * @author JPG
     * @since 2019/11/7 11:46
     * @param roleId
     * @param isSearchCache
     */
    @Override
    public String getBizScopeByRoleId(Long roleId, boolean isSearchCache) {

        Set<Long> set = new HashSet<>();

        //查询角色信息
        ResultData<SelectRoleResp> resultData = sysRoleService.getById(roleId);
        if (resultData == null
                || resultData.getData() == null
                || resultData.getData().getRoleId() == null
                || resultData.getData().getDeptId() == null) {
            return BizScopeEnum.NONE.value;
        }
        Long deptId = resultData.getData().getDeptId();
        //查询机构信息
        ResultData<SelectDeptResp> deptResultDta = sysDeptService.getById(deptId);
        if(deptResultDta == null || deptResultDta.getData() == null){
            return BizScopeEnum.NONE.value;
        }
        if(deptResultDta.getData().getParentId() == null){
            //全国权限
            return BizScopeEnum.CHINA.value;
        }
        //查询当前角色机构下的所有机构
        ResultData<List<SelectDeptResp>> multiLevelResultData = sysDeptService.getMultiLevelDeptList(deptId);
        if(multiLevelResultData == null || CollectionUtils.isEmpty(multiLevelResultData.getData())){
            return BizScopeEnum.NONE.value;
        }
        List<SelectDeptResp> data = multiLevelResultData.getData();
        //获取deptId的Set集合
        Set<Long> collect = data.stream().map(SelectDeptResp::getDeptId).collect(Collectors.toSet());

        //查询全部业务中心ID
        List<Store> storeList = csStoreService.getAll();
        if(storeList == null){
            return BizScopeEnum.NONE.value;
        }
        //计算当前机构下包含的业务中心
        for (Store store : storeList) {
            if(collect.contains(store.getDeptId())){
                set.add(store.getId());
            }
        }
        if(CollectionUtils.isEmpty(set)){
            return BizScopeEnum.NONE.value;
        }

        return Joiner.on(",").join(set);
    }
}
