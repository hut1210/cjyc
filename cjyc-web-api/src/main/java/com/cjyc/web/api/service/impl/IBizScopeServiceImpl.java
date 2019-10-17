package com.cjyc.web.api.service.impl;

import com.cjyc.common.model.dao.IAdminDao;
import com.cjyc.common.model.entity.Admin;
import com.cjyc.common.model.vo.BizScopeVo;
import com.cjyc.web.api.exception.ParameterException;
import com.cjyc.web.api.service.IBizScopeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class IBizScopeServiceImpl implements IBizScopeService {
    @Resource
    private IAdminDao adminDao;

    @Override
    public BizScopeVo getBizScope(Long userId) {
        BizScopeVo bizScopeVo = new BizScopeVo();
        //获取用户权限范围
        Admin admin = adminDao.findByUserId(userId);
        if(admin == null || admin.getBizScope() == null){
            throw new ParameterException("用户无数据权限");
        }
        //业务中心权限
        if(admin.getBizScope() == 0){
            List<Long> list = adminDao.findStoreBizScope(admin.getId());
            bizScopeVo.setBizScopeStoreIds(list);
        }
        //行政区域权限
        if(admin.getBizScope() == 1){
            //暂时无处理
        }
        //TODO 放入缓存
        return bizScopeVo;
    }
}
