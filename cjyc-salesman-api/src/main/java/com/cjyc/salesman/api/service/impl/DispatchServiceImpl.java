package com.cjyc.salesman.api.service.impl;

import com.cjyc.common.model.dao.IOrderCarDao;
import com.cjyc.common.model.dao.IWaybillCarDao;
import com.cjyc.common.model.entity.defined.BizScope;
import com.cjyc.common.model.enums.BizScopeEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.salesman.dispatch.CityCarCountVo;
import com.cjyc.common.model.vo.salesman.dispatch.StartAndEndCityCountVo;
import com.cjyc.common.system.service.sys.ICsSysService;
import com.cjyc.salesman.api.service.IDispatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @Description 调度业务接口实现
 * @Author Liu Xing Xiang
 * @Date 2019/12/11 13:35
 **/
@Service
public class DispatchServiceImpl implements IDispatchService {
    @Autowired
    private ICsSysService csSysService;
    @Autowired
    private IOrderCarDao orderCarDao;
    @Autowired
    private IWaybillCarDao waybillCarDao;

    @Override
    public ResultVo getAllCityCarCount(Long loginId) {
        // 根据登录ID查询当前业务员所在业务中心ID
        BizScope bizScope = csSysService.getBizScopeByLoginId(loginId, true);

        // 判断当前登录人是否有权限访问
        int code = bizScope.getCode();
        if (BizScopeEnum.NONE.code == code) {
            return BaseResultUtil.fail("无权访问");
        }
        if (BizScopeEnum.CHINA.code == code) {
            return BaseResultUtil.fail("暂不支持全国数据访问");
        }

        List<CityCarCountVo> returnList = new ArrayList<>(10);

        // 获取业务中心ID
        String storeIds = getStoreIds(bizScope);
        // 查询出发地相同的车辆数量：提车，干线，送车均未调度的车辆
        List<CityCarCountVo> notDispatchList = orderCarDao.selectStartCityCarCount(storeIds);
        // 查询出发地，目的地相同的车辆数量
        for (CityCarCountVo cityCarCountVo : notDispatchList) {
            List<StartAndEndCityCountVo> startAndEndCityCountList = orderCarDao.selectStartAndEndCityCarCount(cityCarCountVo.getStartCityCode());
            cityCarCountVo.setStartAndEndCityCountList(startAndEndCityCountList);
        }

        returnList.addAll(notDispatchList);


        return BaseResultUtil.success(returnList);
    }

    private String getStoreIds(BizScope bizScope) {
        Set<Long> storeIds = bizScope.getStoreIds();
        StringBuilder sb = new StringBuilder();
        for (Long storeId : storeIds) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(storeId);
        }
        return sb.toString();
    }
}
