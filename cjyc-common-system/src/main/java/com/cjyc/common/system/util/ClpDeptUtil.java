package com.cjyc.common.system.util;

import com.cjkj.common.model.ResultData;
import com.cjkj.common.model.ReturnMsg;
import com.cjkj.usercenter.dto.common.SelectDeptListByParentIdsReq;
import com.cjkj.usercenter.dto.common.SelectDeptResp;
import com.cjyc.common.model.util.YmlProperty;
import com.cjyc.common.system.feign.ISysDeptService;
import com.cjyc.common.system.feign.ISysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 物流平台机构相关
 */

@Component
public class ClpDeptUtil {
    /**
     * 社会车辆事业部机构ID, 因为是初始化数据，所以此ID为固定值
     */
    private static final Long BIZ_TOP_DEPT_ID = Long.parseLong(YmlProperty.get("cjkj.dept_admin_id"));
    @Autowired
    private ISysDeptService sysDeptService;

    /**
     * 获取全国机构id
     * @return
     */
    public Long getCountryGovId() {
        return BIZ_TOP_DEPT_ID;
    }

    /**
     * 获取所有大区机构id
     * @return
     */
    public List<Long> getRegionGovIdList() {
        ResultData<List<SelectDeptResp>> regionGovListRd =
                sysDeptService.getSingleLevelDeptList(BIZ_TOP_DEPT_ID);
        if (!ReturnMsg.SUCCESS.getCode().equals(regionGovListRd.getCode())) {
            return null;
        }
        if (CollectionUtils.isEmpty(regionGovListRd.getData())) {
            return null;
        }
        return regionGovListRd.getData().stream()
                .map(r -> r.getDeptId()).collect(Collectors.toList());
    }

    /**
     * 获取所有省机构id列表
     * @return
     */
    public List<Long> getProvinceGovIdList() {
        //大区信息
        List<Long> regionIdList = getRegionGovIdList();
        return batchQuery(regionIdList);
    }

    /**
     * 获取城市机构id列表信息
     * @return
     */
    public List<Long> getCityGovIdList() {
        List<Long> provinceGovIds = getProvinceGovIdList();
        return batchQuery(provinceGovIds);
    }

    /**
     * 获取业务中心id列表信息
     * @return
     */
    public List<Long> getBizCenterGovIdList() {
        List<Long> cityGovIds = getCityGovIdList();
        return batchQuery(cityGovIds);
    }

    /**
     * 根据父id列表查询子机构列表信息
     * @param parentIdList
     * @return
     */
    private List<Long> batchQuery(List<Long> parentIdList) {
        if (CollectionUtils.isEmpty(parentIdList)) {
            return null;
        }
        List<Long> idList = new ArrayList<>();
        SelectDeptListByParentIdsReq req = new SelectDeptListByParentIdsReq();
        req.setParentIdList(parentIdList);
        ResultData<List<SelectDeptResp>> rd = sysDeptService.getSonDeptsByParentIds(req);
        if (!ResultDataUtil.isSuccess(rd)) {
            return null;
        }else {
            if (!CollectionUtils.isEmpty(rd.getData())) {
                idList.addAll(rd.getData().stream()
                        .map(d -> d.getDeptId()).collect(Collectors.toList()));
            }
        }
        return idList;
    }
}
