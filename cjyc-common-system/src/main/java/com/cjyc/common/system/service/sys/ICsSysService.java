package com.cjyc.common.system.service.sys;

import com.cjyc.common.model.entity.Carrier;
import com.cjyc.common.model.entity.defined.BizScope;
import com.cjyc.common.model.vo.web.mineCarrier.MyCarrierVo;

import java.util.List;

/**
 * fegin接口传参和返回值处理接口
 * @author JPG
 */
public interface ICsSysService {
    @Deprecated
    BizScope getBizScopeByRoleId(Long roleId, boolean isSearchCache);

    @Deprecated
    BizScope getBizScopeByLoginId(Long loginId, boolean isSearchCache);

    @Deprecated
    Carrier getCarrierByRoleId(Long roleId);

    /*********************************韵车集成改版 st*****************************/
    BizScope getBizScopeByRoleIdNew(Long loginId, Long roleId, boolean isSearchCache);
    BizScope getBizScopeBySysRoleIdNew(Long loginId, Long sysRoleId, boolean isSearchCache);

    BizScope getBizScopeByLoginIdNew(Long loginId, boolean isSearchCache);

    List<MyCarrierVo> getCarriersByRoleId(Long loginId, Long roleId);
    /*********************************韵车集成改版 ed*****************************/
}
