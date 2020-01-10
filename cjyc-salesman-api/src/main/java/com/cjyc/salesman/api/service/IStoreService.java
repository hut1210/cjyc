package com.cjyc.salesman.api.service;

import com.cjyc.common.model.entity.Store;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.salesman.store.StoreVo;

import java.util.List;

/**
 *业务员-业务中心
 */
public interface IStoreService {

    /**
     * 根据登录账号查询业务中心
     * @param loginId
     * @return
     */
    ResultVo<List<StoreVo>> listByLoginId(Long loginId);
}
