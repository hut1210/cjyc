package com.cjyc.common.system.service.impl;

import com.cjyc.common.model.dao.IStoreDao;
import com.cjyc.common.model.entity.Store;
import com.cjyc.common.system.service.ICsStoreService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CsStoreServiceImpl implements ICsStoreService {
    @Resource
    private IStoreDao storeDao;

    /**
     * 查询区县所属业务中心-业务范围
     *
     * @param areaCode
     * @author JPG
     * @since 2019/11/5 9:27
     */
    @Override
    public Store findOneBelongByAreaCode(String areaCode) {
        return storeDao.findOneBelongByAreaCode(areaCode);
    }

    /**
     * 查询区县所属业务中心列表-业务范围
     *
     * @param areaCode
     * @author JPG
     * @since 2019/11/5 9:27
     */
    @Override
    public List<Store> findBelongByAreaCode(String areaCode) {
        return storeDao.findBelongByAreaCode(areaCode);
    }

    /**
     * 根据ID查询业务中心
     *
     * @param storeId
     * @param isSearchCache
     * @author JPG
     * @since 2019/11/5 17:26
     */
    @Override
    public Store getById(Long storeId, boolean isSearchCache) {
        return storeDao.selectById(storeId);
    }

    /**
     * 根据ID查询业务中心覆盖范围
     *
     * @param storeId
     * @return
     */
    @Override
    public List<String> findAreaBizScope(Long storeId) {
        return storeDao.findAreaBizScope(storeId);
    }

    @Override
    public List<Store> getAll() {
        return storeDao.findAll();
    }

    @Override
    public List<Long> getAllDeptId() {
        return storeDao.findAllDeptId();
    }
}