package com.cjyc.salesman.api.service.impl;

import com.cjyc.common.model.dao.IOrderCarDao;
import com.cjyc.common.model.entity.defined.BizScope;
import com.cjyc.common.model.enums.BizScopeEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.salesman.dispatch.StartCityVo;
import com.cjyc.common.system.service.sys.ICsSysService;
import com.cjyc.salesman.api.service.IDispatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public ResultVo getAllCityCarCount(Long loginId) {
        // 根据登录ID查询当前业务员所在业务中心ID
        BizScope bizScope = csSysService.getBizScopeByLoginId(loginId, true);
        int code = bizScope.getCode();
        if (BizScopeEnum.NONE.code == code) {
            return BaseResultUtil.fail("无权访问");
        }
        if (BizScopeEnum.CHINA.code == code) {
            return BaseResultUtil.fail("暂不支持全国数据访问");
        }

        Set<Long> storeIds = bizScope.getStoreIds();
        StringBuilder sb = new StringBuilder();
        for (Long storeId : storeIds) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(storeId);
        }
        // 查询业务中心下所有车辆的出发地和目的地
        List<StartCityVo> list = orderCarDao.selectAllCarCity(sb.toString());



        return null;
    }
}
